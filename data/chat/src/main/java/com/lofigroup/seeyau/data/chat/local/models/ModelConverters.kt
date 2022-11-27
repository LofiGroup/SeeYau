package com.lofigroup.seeyau.data.chat.local.models

import com.lofigroup.seeyau.domain.chat.models.*
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.profile.model.Like

fun Like.toChatMessage(): ChatMessage {
  return ChatMessage(
    author = userId,
    createdIn = createdIn,
    status = MessageStatus.READ,
    type = MessageType.Like,
    id = id,
    message = ""
  )
}

fun ChatMessageRequest.toLocalMessage(id: Long, type: MessageTypeEntity) = MessageEntity(
  id = id,
  author = 0L,
  chatId = chatId,
  createdIn = System.currentTimeMillis(),
  isRead = false,
  message = message,
  type = type,
  extra = mediaUri.toString()
)

fun MessageEntity.toNewMessageEvent() = NewChatMessage(
  authorIsMe = author == 0L,
  chatId = chatId
)

fun ChatEntity.toDomainModel() = Chat(
  id = id,
  draft = draft.message
)