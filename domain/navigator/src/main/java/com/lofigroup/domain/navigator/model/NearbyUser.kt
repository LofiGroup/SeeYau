package com.lofigroup.domain.navigator.model

import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class NearbyUser(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val isLikedByMe: Boolean,
  val likesCount: Int,
  val lastContact: Long,
  val isOnline: Boolean,
  val newMessages: List<ChatMessage>
)