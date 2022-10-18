package com.lofigroup.seeyau.domain.chat.models

data class ChatDraft(
  val message: String,
  val createdIn: Long,
  val chatId: Long
)
