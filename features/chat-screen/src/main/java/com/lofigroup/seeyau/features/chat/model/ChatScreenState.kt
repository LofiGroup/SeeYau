package com.lofigroup.seeyau.features.chat.model

import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User

data class ChatScreenState(
  val partner: User = User(id = -1, name = "", imageUrl = null, lastConnection = 0L, isNear = false),
  val messages: List<PrivateMessage> = emptyList(),
  val message: String = ""
)

fun Chat.toChatScreenState() = ChatScreenState(
  partner = partner,
  messages = messages.map { it.toPrivateMessage() }
)
