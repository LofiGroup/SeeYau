package com.lofigroup.seeyau.data.chat.local.models

import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.Like

fun Like.toChatMessage(): ChatMessage {
  return ChatMessage.LikeMessage(
    author = userId,
    createdIn = createdIn,
    isRead = true
  )
}

