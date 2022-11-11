package com.lofigroup.seeyau.features.chat_screen.model

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus

data class ChatListScreenState(
  val errorMessage: String? = null,

  val memoryFolder: List<FolderChat.MemoryChat> = listOf(),
  val likesFolder: List<FolderChat.LikeChat> = listOf(),
  val interactionFolder: List<FolderChat.DefaultChat> = listOf(),
  val newMessagesCount: Int = 0
)

