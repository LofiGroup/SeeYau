package com.lofigroup.seeyau.domain.chat.models

data class ChatMessageRequest(
  val message: String,
  val chatId: Long
)
