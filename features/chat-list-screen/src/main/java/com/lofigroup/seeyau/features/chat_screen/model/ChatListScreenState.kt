package com.lofigroup.seeyau.features.chat_screen.model

import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief

data class ChatListScreenState(
  val errorMessage: String? = null,
  val chats: List<ChatBrief> = listOf()
)
