package com.lofigroup.seeyau.domain.chat.models.events

sealed class ChatEvent(
  val chatId: Long
)