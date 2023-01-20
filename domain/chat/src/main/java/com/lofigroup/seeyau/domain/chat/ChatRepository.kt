package com.lofigroup.seeyau.domain.chat

import com.lofigroup.seeyau.domain.chat.models.*
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

  suspend fun pullData(returnResult: Boolean): List<ChatNewMessages>
  suspend fun sendLocalMessages()

  fun observeChats(): Flow<List<ChatBrief>>
  suspend fun getChat(chatId: Long): Chat
  fun observeChatEvents(): Flow<ChatEvent>
  fun observeChatMessages(chatId: Long): Flow<List<ChatMessage>>

  suspend fun sendMessage(messageRequest: ChatMessageRequest)
  suspend fun markChatAsRead(chatId: Long)

  suspend fun getUserIdByChatId(chatId: Long): Long?
  suspend fun getChatIdByUserId(userId: Long): Long?

  suspend fun updateChatDraft(chatDraftUpdate: ChatDraftUpdate)

}