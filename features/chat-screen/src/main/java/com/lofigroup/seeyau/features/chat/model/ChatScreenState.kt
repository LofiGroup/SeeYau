package com.lofigroup.seeyau.features.chat.model

import com.lofigroup.domain.navigator.model.User
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class ChatScreenState(
  val partner: User = User(id = -1, name = "", imageUrl = null, lastConnection = 0L, isNear = false),
  val messages: List<PrivateMessage> = emptyList()
)

fun Chat.toChatScreenState() = ChatScreenState(
  partner = partner,
  messages = messages.map { it.toPrivateMessage() }
)
