package com.lofigroup.seeyau.features.chat_screen.model

import com.lofigroup.seeyau.domain.chat.models.ChatBrief

data class ChatListScreenState(
  val errorMessage: String? = null,

  val memoryFolder: List<ChatBrief> = listOf(),
  val likesFolder: List<ChatBrief> = listOf(),
  val interactionFolder: List<ChatBrief> = listOf(),
  val newMessagesCount: Int = 0
)