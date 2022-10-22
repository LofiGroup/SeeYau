package com.lofigroup.seeyau.data.chat

import com.lofigroup.core.util.addToOrderedDesc
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.models.*
import com.lofigroup.seeyau.data.chat.remote.websocket.ChatWebSocketListener
import com.lofigroup.seeyau.data.chat.remote.websocket.models.toWebSocketRequest
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.model.extractLike
import com.lofigroup.seeyau.data.profile.local.model.toUser
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.*
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.profile.model.Like
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImpl @Inject constructor(
  private val chatDataHandler: ChatDataHandler,
  private val chatDao: ChatDao,
  private val profileDataHandler: ProfileDataHandler,
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
          profileDataHandler.observeAssembledUser(chat.partnerId),
          chatDao.observeLastMessage(chat.id),
          chatDao.observeUserNewMessages(chat.partnerId),
        ) { user, lastMessage, newMessages ->
          ChatBrief(
            id = chat.id,
            partner = user.toUser(),
            lastMessage = getLastMessage(lastMessage, user.extractLike()),
            newMessagesCount = newMessages.size,
            draft = chat.draft,
          )
        }
      }) { it.asList() }
    }
  }

  override fun observeChat(chatId: Long): Flow<Chat> {
    return chatDao.observeChat(chatId).flatMapLatest { chat ->
      combine(
        chatDao.observeChatMessages(chat.id),
        profileDataHandler.observeUserLike(chat.partnerId)
      ) { messages, like ->
        Chat(
          id = chat.id,
          messages = messages
            .map { it.toDomainModel() }
            .addToOrderedDesc(like?.toChatMessage()) { it.createdIn },
          draft = chat.draft
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

  override suspend fun updateChatDraft(chatDraft: ChatDraft) {
    safeIOCall(ioDispatcher) {
      chatDao.insertDraft(chatDraft.message, chatDraft.chatId)
    }
  }

  private fun getLastMessage(lastMessage: MessageEntity?, like: Like?): ChatMessage? {
    val lastMessageCreatedIn = lastMessage?.createdIn ?: 0L
    val likeCreatedIn = like?.createdIn ?: 0L

    return if (lastMessageCreatedIn >= likeCreatedIn) lastMessage?.toDomainModel()
      else like?.toChatMessage()
  }

}