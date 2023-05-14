package com.lofigroup.seeyau.data.chat.local.models

import android.content.Context
import com.lofigroup.seeyau.data.chat.R
import com.lofigroup.seeyau.domain.chat.models.*
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.profile.model.Like

fun Like.toChatMessage(chatId: Long): ChatMessage {
  return ChatMessage(
    author = userId,
    createdIn = createdIn,
    status = MessageStatus.READ,
    type = MessageType.Like,
    id = id,
    message = "",
    chatId = chatId
  )
}

fun ChatMessageRequest.toLocalMessage(id: Long, type: MessageTypeEntity, context: Context) = MessageEntity(
  id = id,
  author = 0L,
  chatId = chatId,
  createdIn = System.currentTimeMillis(),
  isRead = false,
  message = message,
  type = type,
  extra = extractExtraFromUri(mediaUri, type, context)
)

fun MessageEntity.toNewMessageEvent() = NewChatMessage(
  authorIsMe = author == 0L,
  chatId = chatId
)

fun ChatEntity.toDomainModel() = Chat(
  id = id,
  draft = draft.message
)

fun MessageEntity.toTextMessage(context: Context) = TextMessage(
  id = id,
  chatId = chatId,
  createdIn = createdIn,
  text = when (type) {
    MessageTypeEntity.PLAIN -> message
    MessageTypeEntity.AUDIO -> context.getString(R.string.headphones)
    MessageTypeEntity.IMAGE -> context.getString(R.string.picture)
    MessageTypeEntity.CONTACT -> ""
    MessageTypeEntity.VIDEO -> context.getString(R.string.video)
  }
)