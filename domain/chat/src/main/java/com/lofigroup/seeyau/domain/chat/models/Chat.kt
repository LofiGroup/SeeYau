package com.lofigroup.seeyau.domain.chat.models


data class Chat(
  val id: Long,
  val messages: List<ChatMessage>,
  val draft: String
)
