package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.LastChatUpdateDataSource
import com.lofigroup.seeyau.data.chat.local.models.toDomainModel
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toChatEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.ChatWebSocketChannel
import com.lofigroup.seeyau.data.chat.remote.websocket.models.toWebSocketRequest
import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
  private val chatApi: ChatApi,
  private val chatDao: ChatDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val lastChatUpdateDataSource: LastChatUpdateDataSource,
  private val profileDataSource: ProfileDataSource,
  private val chatWebSocket: ChatWebSocketChannel
): ChatRepository {

  override suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val fromDate = getLastMessageDate()
      Timber.e("From date: $fromDate")

      val response = retrofitErrorHandler(chatApi.getChatUpdates(fromDate))
      for (chatUpdate in response)
        insertUpdates(chatUpdate)
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }

    chatWebSocket.connect()
  }

  override suspend fun sendMessage(messageRequest: ChatMessageRequest) = withContext(ioDispatcher) {
    chatWebSocket.sendMessage(messageRequest.toWebSocketRequest())
  }

  override suspend fun markChatAsRead(chatId: Long) = withContext(ioDispatcher) {
    chatWebSocket.markChatAsRead(chatId)
  }

  override fun getChats(): Flow<List<Chat>> {
    return chatDao.getChats().map { it.map { chat -> chat.toDomainModel() } }
  }

  override fun getChat(id: Long): Flow<Chat> {
    return chatDao.getAssembledChat(id).map { it.toDomainModel() }
  }

  private suspend fun insertUpdates(chatUpdates: ChatUpdatesDto) {
    val chat = chatUpdates.toChatEntity()
    val messages = chatUpdates.newMessages.map { it.toMessageEntity(myId = profileDataSource.getMyId()) }
    chatDao.insertChat(chat)
    chatDao.insertMessages(messages)
  }

  private suspend fun getLastMessageDate(): Long {
    val lastMessage = chatDao.getLastMessage()
    return lastMessage?.createdIn ?: 0L
  }

}