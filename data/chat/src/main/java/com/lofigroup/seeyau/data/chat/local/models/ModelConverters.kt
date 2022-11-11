package com.lofigroup.seeyau.data.chat.local.models

import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.profile.model.Like

fun Like.toChatMessage(): ChatMessage {
  return ChatMessage.LikeMessage(
    author = userId,
    createdIn = createdIn,
    status = MessageStatus.READ
  )
}

fun ChatMessageRequest.toLocalMessage(id: Long) = MessageEntity(
  id = id,
  author = 0L,
  chatId = chatId,
  createdIn = System.currentTimeMillis(),
  isRead = false,
  message = message
)

fun MessageEntity.toNewMessageEvent() = NewChatMessage(
  authorIsMe = author == 0L,
  chatId = chatId
)

fun ChatEntity.toDomainModel() = Chat(
  id = id,
  draft = draft.message
)