package com.lofigroup.seeyau.features.chat_screen.ui

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import kotlinx.coroutines.flow.Flow

interface ChatListScreenStateHolder {

  fun getState(): Flow<ChatListScreenState>

  fun blacklistUser(userId: Long)

  fun setCurrentChat(chatBrief: ChatBrief?)

  fun likeUser(isLiked: Boolean, userId: Long)
}

private fun getPreviewChatBrief(message: String = "Hello"): ChatBrief {
  return ChatBrief(
    id = 1,
    lastMessage = ChatMessage(id = 0, message = message, author = 0, createdIn = 0L, status = MessageStatus.READ, type = MessageType.Plain),
    partner = getUserPreviewModel(),
    newMessagesCount = 1,
    createdIn = 0L
  )
}

val previewState = ChatListScreenState(
  interactionFolder = listOf(
    getPreviewChatBrief(),
    getPreviewChatBrief(message = "See you later alligator")
  )
)