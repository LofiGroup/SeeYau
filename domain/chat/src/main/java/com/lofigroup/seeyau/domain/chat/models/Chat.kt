package com.lofigroup.seeyau.domain.chat.models

import com.lofigroup.seeyau.domain.profile.model.User


data class Chat(
  val id: Long,
  val partner: User,
  val messages: List<ChatMessage>
)
