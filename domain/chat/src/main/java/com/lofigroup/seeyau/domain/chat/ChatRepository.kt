package com.lofigroup.seeyau.domain.chat

import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

  suspend fun pullData()

  fun observeChats(): Flow<List<ChatBrief>>
  fun observeChat(chatId: Long): Flow<Chat>
  fun observeChatEvents(chatId: Long): Flow<ChatEvent>

  suspend fun sendMessage(messageRequest: ChatMessageRequest)
  suspend fun markChatAsRead(chatId: Long)

  suspend fun getUserIdByChatId(chatId: Long): Long?
  suspend fun getChatIdByUserId(userId: Long): Long?

}