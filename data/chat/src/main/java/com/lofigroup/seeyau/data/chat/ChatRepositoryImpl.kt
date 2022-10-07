package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.models.toDomainModel
import com.lofigroup.seeyau.data.chat.remote.websocket.ChatWebSocketListener
import com.lofigroup.seeyau.data.chat.remote.websocket.models.toWebSocketRequest
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImpl @Inject constructor(
  private val chatDataHandler: ChatDataHandler,
  private val chatDao: ChatDao,
  private val userDao: UserDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val chatWebSocket: ChatWebSocketListener,
  private val eventsDataSource: EventsDataSource
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

  override fun observeChats(): Flow<List<ChatBrief>> {
    return chatDao.observeChats().flatMapLatest { chats ->
      combine(chats.map { chat ->
        combine(
          userDao.observeUser(chat.partnerId),
          chatDao.observeLastMessage(chat.id),
          chatDao.observeUserNewMessages(chat.partnerId)
        ) { user, lastMessage, newMessages ->
          Timber.e("New messages: $newMessages, chat_id = ${chat.id}")
          ChatBrief(
            id = chat.id,
            partner = user.toDomainModel(),
            lastMessage = lastMessage?.toDomainModel(),
            newMessagesCount = newMessages.size
          )
        }
      }) { it.asList() }
    }
  }

  override fun observeChat(chatId: Long): Flow<Chat> {
    return chatDao.observeChat(chatId).flatMapLatest { chat ->
      chatDao.observeChatMessages(chat.id).map { messages ->
        Chat(
          id = chat.id,
          messages = messages.map { it.toDomainModel()}
        )
      }
    }
  }

  override fun observeChatEvents(chatId: Long): Flow<ChatEvent> {
    return eventsDataSource.observe(chatId)
  }

  override suspend fun getUserIdByChatId(chatId: Long): Long? {
    return chatDao.getUserIdFromChatId(chatId)
  }

  override suspend fun getChatIdByUserId(userId: Long): Long? {
    return chatDao.getChatIdFromUserId(userId)
  }

}