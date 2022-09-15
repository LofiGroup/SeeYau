package com.lofigroup.seeyau.features.chat.model

import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class PrivateMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val message: String,
  val createdIn: Long
)

fun ChatMessage.toPrivateMessage() = PrivateMessage(
  authorIsMe = author == 0L,
  message = message,
  createdIn = createdIn,
  id = id
)
