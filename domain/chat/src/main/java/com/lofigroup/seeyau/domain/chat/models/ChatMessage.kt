package com.lofigroup.seeyau.domain.chat.models

data class ChatMessage(
  val id: Long,
  val message: String,
  val author: Long,
  val createdIn: Long,
  val isRead: Boolean
)