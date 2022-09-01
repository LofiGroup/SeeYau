package com.lofigroup.seeyau.domain.chat.models

import com.lofigroup.domain.navigator.model.User

data class Chat(
  val id: Long,
  val user: User,
  val messages: List<ChatMessage>
)
