package com.lofigroup.seeyau.domain.chat.models.events

import com.lofigroup.seeyau.domain.chat.models.ChatMessage

class NewChatMessage(
  val authorIsMe: Boolean
): ChatEvent
