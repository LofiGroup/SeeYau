package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.LastChatUpdateDataSource
import com.lofigroup.seeyau.data.chat.local.models.toDomainModel
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toChatEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.ChatWebSocketListener
import com.lofigroup.seeyau.data.chat.remote.websocket.models.toWebSocketRequest
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
  private val chatDataHandler: ChatDataHandler,
  private val chatDao: ChatDao,
  private val userDao: UserDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val chatWebSocket: ChatWebSocketListener
): ChatRepository {
  override suspend fun pullData() {
    chatDataHandler.pullData()
  }

  override suspend fun sendMessage(messageRequest: ChatMessageRequest) = withContext(ioDispatcher) {
    chatWebSocket.sendMessage(messageRequest.toWebSocketRequest())
  }

  override suspend fun markChatAsRead(chatId: Long) = withContext(ioDispatcher) {
    chatWebSocket.markChatAsRead(chatId)
  }

  override fun getChats(): Flow<List<ChatBrief>> {
    return chatDao.getChats().flatMapLatest { chats ->
      combine(chats.map { chat ->
        combine(
          userDao.observeUser(chat.partnerId),
          chatDao.observeLastMessage(chat.id),
          chatDao.getNewUserMessages(chat.partnerId)
        ) { user, lastMessage, newMessages ->
          ChatBrief(
            id = chat.id,
            partner = user.toDomainModel(),
            lastMessage = lastMessage?.toDomainModel(chat.partnerLastVisited),
            newMessagesCount = newMessages.size
          )
        }
      }) { it.asList() }
    }
  }

  override fun getChat(id: Long): Flow<Chat> {
    return chatDao.getChat(id).flatMapLatest { chat ->
      userDao.observeUser(chat.partnerId).combine(chatDao.getChatMessages(chat.id)) { user, messages ->
        Chat(
          id = chat.id,
          partner = user.toDomainModel(),
          messages = messages.map { it.toDomainModel(chat.partnerLastVisited)}
        )
      }
    }
  }

}