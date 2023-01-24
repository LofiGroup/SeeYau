package com.lofigroup.seeyau.domain.chat.models

import com.lofigroup.seeyau.domain.profile.model.User

data class ChatNewMessages(
  val chatMessages: List<TextMessage>,
  val partner: User,
  val chatId: Long,
  val count: Int
)
