package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.chat.local.models.toLocalMessage
import com.lofigroup.seeyau.data.chat.local.models.toNewMessageEvent
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toChatEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.SendMessageWsRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.MessageIsReceivedResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.NewMessageWsResponse
import com.lofigroup.seeyau.data.chat.remote.websocket.models.toWebSocketRequest
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
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
  private val eventsDataSource: EventsDataSource
) {

  suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val fromDate = chatDao.getLastMessageCreatedIn() ?: 0L
      Timber.e("From date: $fromDate")

      val response = retrofitErrorHandler(chatApi.getChatUpdates(fromDate))
      Timber.e("Chat updates: $response")
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
        eventsDataSource.onChatIsReadEvent(response)
      }
    }
  }

  suspend fun createLocalMessage(request: ChatMessageRequest): SendMessageWsRequest {
    val id = (chatDao.getLastLocalMessageId() ?: (MessageEntity.SEND_ID_OFFSET - 1)) + 1
    chatDao.insertMessage(request.toLocalMessage(id))
    eventsDataSource.onNewMessageEvent(NewChatMessage(authorIsMe = true, chatId = request.chatId))

    return request.toWebSocketRequest(id)
  }

  fun saveMessage(webSocketResponse: NewMessageWsResponse) {
    ioScope.launch(ioDispatcher) {
      val message = webSocketResponse.messageDto.toMessageEntity(
        myId = profileDataHandler.getMyId(),
        readIn = 0L
      )
      chatDao.insertNewMessage(message)

      eventsDataSource.onNewMessageEvent(message.toNewMessageEvent())
    }
  }

  fun saveLocalMessage(response: MessageIsReceivedResponse) {
    ioScope.launch(ioDispatcher) {
      val message = chatDao.insertSentMessage(response.localId, response.realId, response.createdIn) ?: return@launch
      eventsDataSource.onNewMessageEvent(message.toNewMessageEvent())
    }
  }

  private suspend fun insertUpdates(chatUpdates: ChatUpdatesDto) {
    val chat = chatUpdates.toChatEntity()
    val messages = chatUpdates.newMessages.map { it.toMessageEntity(myId = profileDataHandler.getMyId(), readIn = chat.partnerLastVisited) }
    chatDao.upsertChat(chat)
    chatDao.insertMessages(messages)
  }
}