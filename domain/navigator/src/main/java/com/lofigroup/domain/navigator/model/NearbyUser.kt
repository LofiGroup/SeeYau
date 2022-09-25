package com.lofigroup.domain.navigator.model

import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class NearbyUser(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long,
  val isNear: Boolean,
  val newMessages: List<ChatMessage>
)