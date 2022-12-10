package com.lofigroup.seeyau.features.chat_screen.model

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.profile.model.Profile

data class ChatListScreenState(
  val errorMessage: String? = null,
  val profile: Profile = Profile(0L, "", "", 0),

  val nearbyFolder: List<ChatBrief> = listOf(),
  val metFolder: List<ChatBrief> = listOf(),
  val interactionFolder: List<ChatBrief> = listOf(),
  val newMessagesCount: Int = 0,

  val currentItem: ChatBrief? = null
)

