package com.lofigroup.seeyau.domain.chat.models

import com.lofigroup.seeyau.domain.profile.model.User

data class ChatBrief(
  val id: Long,
  val partner: User,
  val lastMessage: ChatMessage?,
  val newMessagesCount: Int,
  val draft: String
)
