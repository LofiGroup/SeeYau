package com.lofigroup.seeyau.features.chat.model

import com.lofigroup.seeyau.domain.profile.model.User

data class ChatScreenState(
  val partner: User = User(id = -1, name = "", imageUrl = null, lastConnection = 0L, isNear = false, isOnline = false),
  val messages: Map<String, List<UIChatMessage>> = emptyMap(),
  val message: String = "",
  val currentItemPos: Int = -1
)
