package com.lofigroup.seeyau.domain.chat.models.events

class NewChatMessage(
  val authorIsMe: Boolean,
  chatId: Long
): ChatEvent(chatId)
