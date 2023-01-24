package com.lofigroup.seeyau.domain.chat.models

data class TextMessage(
  val id: Long,
  val text: String,
  val chatId: Long,
  val createdIn: Long
)
