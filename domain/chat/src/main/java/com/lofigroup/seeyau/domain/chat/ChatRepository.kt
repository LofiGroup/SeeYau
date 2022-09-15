package com.lofigroup.seeyau.domain.chat

import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

  suspend fun pullData()

  suspend fun sendMessage(messageRequest: ChatMessageRequest)

  fun getChats(): Flow<List<Chat>>

  fun getChat(id: Long): Flow<Chat>

}