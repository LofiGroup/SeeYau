package com.lofigroup.seeyau.data.chat

import android.content.Context
import com.lofigroup.core.util.addToOrderedDesc
import com.lofigroup.seeyau.common.chat.components.notifications.ChatNotificationBuilder
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.models.*
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.SendMessageDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toForm
import com.lofigroup.seeyau.data.chat.remote.http.models.toSendMessageDto
import com.lofigroup.seeyau.data.chat.remote.websocket.ChatWebSocketListener
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.toSendMessageRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.toSendMessageWsRequest
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.model.extractLike
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.data.profile.local.model.toUser
import com.lofigroup.seeyau.domain.base.user_notification_channel.UserNotificationChannel
import com.lofigroup.seeyau.domain.base.user_notification_channel.model.UserNotification
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.*
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.profile.model.Like
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.createMultipartBody
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImpl @Inject constructor(
  private val chatDataHandler: ChatDataHandler,
  private val chatDao: ChatDao,
  private val chatApi: ChatApi,
  private val profileDataHandler: ProfileDataHandler,
  private val ioDispatcher: CoroutineDispatcher,
  private val chatWebSocket: ChatWebSocketListener,
  private val eventsDataSource: EventsDataSource,
  private val context: Context,
  private val userNotificationChannel: UserNotificationChannel,
  private val chatNotificationBuilder: ChatNotificationBuilder,
  private val scope: CoroutineScope,
  private val downloader: FileDownloader
) : ChatRepository {

  override suspend fun pullData() {
    return chatDataHandler.pullData()
  }

  override suspend fun sendLocalMessages() {
    safeIOCall(ioDispatcher) {
      val messages = chatDao.getLocalMessages()
      for (message in messages) {
        if (message.type == MessageTypeEntity.PLAIN)
          chatWebSocket.sendMessage(message.toSendMessageRequest())
        else
          sendMessageWithMedia(message.toSendMessageDto())
      }
    }
  }

  override suspend fun sendMessage(messageRequest: ChatMessageRequest) {
    safeIOCall(ioDispatcher) {
      val request = chatDataHandler.createLocalMessage(messageRequest)

      if (request.type == "plain") {
        chatWebSocket.sendMessage(request.toSendMessageWsRequest())
      } else {
        sendMessageWithMedia(request)
      }
    }
  }

  override suspend fun markChatAsRead(chatId: Long) {
    safeIOCall(ioDispatcher) {
      chatWebSocket.markChatAsRead(chatId)
      chatNotificationBuilder.removeChatNotification(chatId)
    }
  }

  override fun observeChats(): Flow<List<ChatBrief>> {
    return chatDao.observeChats().flatMapLatest { chats ->
      combine(chats.map { chat ->
        combine(
          profileDataHandler.observeAssembledUser(chat.partnerId),
          chatDao.observeLastMessage(chat.id),
          chatDao.observeUserNewMessages(chat.partnerId),
        ) { user, lastMessage, newMessages ->
          ChatBrief(
            id = chat.id,
            partner = user.toUser(),
            lastMessage = getLastMessage(lastMessage, user.extractLike(), chat.id),
            newMessagesCount = newMessages.size,
            draft = chat.draft.toDomainModel(),
            createdIn = chat.createdIn,
          )
        }
      }) { it.asList() }
    }
  }

  override suspend fun getChat(chatId: Long): Chat {
    return chatDao.getChat(chatId).toDomainModel()
  }

  override fun observeChatMessages(chatId: Long): Flow<List<ChatMessage>> {
    return chatDao.observeChat(chatId).flatMapLatest { chat ->
      combine(
        chatDao.observeChatMessages(chat.id),
        profileDataHandler.observeUserLike(chat.partnerId)
      ) { messages, like ->
        messages.map { it.toDomainModel() }
          .addToOrderedDesc(like?.toChatMessage(chatId)) { it.createdIn }
      }
    }
  }

  override fun observeChatEvents(): Flow<ChatEvent> {
    return eventsDataSource.observe()
  }

  override suspend fun getUserIdByChatId(chatId: Long) = chatDataHandler.getUserIdByChatId(chatId)

  override suspend fun getChatIdByUserId(userId: Long) = chatDataHandler.getChatIdByUserId(userId)

  override suspend fun getNewMessages(): List<ChatNewMessages> = withContext(ioDispatcher) {
    val newMessages = chatDao.getNewMessages()

    val updates = newMessages.map { it.toTextMessage(context) }.groupBy { it.chatId }

    updates.map {
      ChatNewMessages(
        chatMessages = it.value,
        partner = chatDao.getUserFromChatId(chatId = it.key).toDomainModel(),
        chatId = it.key,
        count = it.value.size
      )
    }
  }

  override suspend fun updateChatDraft(chatDraftUpdate: ChatDraftUpdate) {
    safeIOCall(ioDispatcher) {
      chatDao.insertDraft(chatDraftUpdate.toDraftUpdate())
    }
  }

  override fun downloadMediaForMessage(messageId: Long) {
    scope.launch(ioDispatcher) {
      val message = chatDao.getMessage(messageId) ?: return@launch
      val extra = message.extra ?: return@launch

      val url = extractMediaUri(extra) ?: return@launch

      Timber.e("Starting media download for message with id: $messageId")
      val newUri = downloader.downloadToInternalStorage("message_${messageId}", url) ?: return@launch

      val newExtra = updateExtraUri(newUri, extra, message.type) ?: return@launch

      chatDao.updateMessageExtra(
        MessageExtraUpdate(
          id = messageId,
          extra = newExtra
        )
      )
    }
  }

  private fun getLastMessage(lastMessage: MessageEntity?, like: Like?, chatId: Long): ChatMessage? {
    val lastMessageCreatedIn = lastMessage?.createdIn ?: 0L
    val likeCreatedIn = like?.createdIn ?: 0L

    return if (lastMessageCreatedIn >= likeCreatedIn) lastMessage?.toDomainModel()
    else like?.toChatMessage(chatId)
  }

  private suspend fun sendMessageWithMedia(request: SendMessageDto) {
    val multipart = createMultipartBody("media", request.mediaUri, contentResolver = context.contentResolver)
      ?: return

    try {
      val response = retrofitErrorHandler(chatApi.sendChatMedia(
        form = request.toForm(),
        media = multipart
      ))

      chatDataHandler.saveLocalMessage(response)
    } catch (e: HttpException) {
      if (e.code() == 413) {
        chatDataHandler.deleteMessage(request.localId)
        userNotificationChannel.sendNotification(UserNotification(message = context.getString(R.string.file_is_too_large)))
      }
      else throw e
    }


  }

}