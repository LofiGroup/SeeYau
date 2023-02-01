package com.lofigroup.seeyau.data.chat

import android.content.Context
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.models.*
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.*
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.MessageIsReceivedResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.NewMessageWsResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.toChatEvent
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.lofigroup.seeyau.domain.chat.models.ChatNewMessages
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ChatDataHandler @Inject constructor(
  private val chatApi: ChatApi,
  private val chatDao: ChatDao,
  private val profileDataHandler: ProfileDataHandler,
  private val ioDispatcher: CoroutineDispatcher,
  private val ioScope: CoroutineScope,
  private val eventsDataSource: EventsDataSource,
  private val context: Context
) {

  suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val fromDate = chatDao.getLastMessageCreatedIn() ?: 0L

      val response = retrofitErrorHandler(chatApi.getChatUpdates(fromDate))
      for (chatUpdate in response)
        insertUpdates(chatUpdate)
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  fun pullChatData(chatId: Long, fromDate: Long = 0L) {
    ioScope.launch(ioDispatcher) {
      try {
        val response = retrofitErrorHandler(chatApi.getChatData(chatId = chatId, fromDate = fromDate))
        profileDataHandler.pullUserData(response.partnerId)
        insertUpdates(response)
      }
      catch (e: Exception) {
        Timber.e(getErrorMessage(e))
      }
    }
  }

  fun observeUserNewMessages(userId: Long): Flow<List<MessageEntity>> {
    return chatDao.observeUserNewMessages(userId)
  }

  fun onChatIsRead(response: ChatIsReadWsResponse) {
    ioScope.launch(ioDispatcher) {
      if (response.userId == profileDataHandler.getMyId()) {
        chatDao.updateChatLastVisited(response.chatId, response.readIn)
      } else {
        chatDao.markMessages(response.chatId)
        chatDao.updateChatPartnerLastVisited(response.chatId, response.readIn)
        eventsDataSource.onEvent(response.toChatEvent())
      }
    }
  }

  suspend fun createLocalMessage(request: ChatMessageRequest): SendMessageDto {
    val id = (chatDao.getLastLocalMessageId() ?: (MessageEntity.LOCAL_MESSAGES_ID_OFFSET - 1)) + 1

    val messageType = resolveMessageType(request.mediaUri, context)
    val messageEntity = request.toLocalMessage(id = id, type = messageType)
    insertMessage(messageEntity)

    return messageEntity.toSendMessageDto()
  }

  suspend fun deleteMessage(messageId: Long) {
    chatDao.deleteMessage(messageId)
  }

  fun saveMessage(webSocketResponse: NewMessageWsResponse) {
    ioScope.launch(ioDispatcher) {
      val message = webSocketResponse.messageDto.toMessageEntity(
        myId = profileDataHandler.getMyId(),
        readIn = 0L
      )
      chatDao.insertNewMessage(message)

      eventsDataSource.onEvent(message.toNewMessageEvent())
    }
  }

  fun saveLocalMessage(response: MessageIsReceivedResponse) {
    ioScope.launch(ioDispatcher) {
      val message = chatDao.insertSentMessage(response.localId, response.realId, response.createdIn) ?: return@launch
      eventsDataSource.onEvent(message.toNewMessageEvent())
    }
  }

  private suspend fun insertMessage(message: MessageEntity) {
    chatDao.insertMessage(message)
    eventsDataSource.onEvent(NewChatMessage(authorIsMe = true, chatId = message.chatId))
  }

  private suspend fun insertUpdates(chatUpdates: ChatUpdatesDto) {
    val chat = chatUpdates.toChatEntity()
    val messages = chatUpdates.newMessages.map { it.toMessageEntity(myId = profileDataHandler.getMyId(), readIn = chat.partnerLastVisited) }
    chatDao.upsertChat(chat)
    chatDao.insertMessages(messages)
  }

  suspend fun getUserIdByChatId(chatId: Long) = safeIOCall(ioDispatcher) {
    chatDao.getUserIdFromChatId(chatId)
  }

  suspend fun getChatIdByUserId(userId: Long) = safeIOCall(ioDispatcher) {
    chatDao.getChatIdFromUserId(userId)
  }
}