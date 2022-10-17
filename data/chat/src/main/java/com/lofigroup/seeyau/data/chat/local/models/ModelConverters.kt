package com.lofigroup.seeyau.data.chat.local.models

import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.lofigroup.seeyau.domain.chat.models.ChatMessage

fun LikeEntity.toChatMessage(): ChatMessage {
  return ChatMessage.LikeMessage(
    author = byWho,
    createdIn = updatedIn,
    isRead = true
  )
}