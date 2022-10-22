package com.lofigroup.seeyau.features.chat_screen.ui

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import kotlinx.coroutines.flow.Flow

interface ChatListScreenStateHolder {

  fun getState(): Flow<ChatListScreenState>

}

private fun getPreviewChatBrief(message: String = "Hello"): ChatBrief {
  return ChatBrief(
    id = 1,
    lastMessage = ChatMessage.PlainMessage(id = 0, message = message, author = 0, createdIn = 0L, isRead = true),
    partner = getUserPreviewModel(),
    newMessagesCount = 1,
    draft = "",
  )
}

val previewState = ChatListScreenState(
  interactionFolder = listOf(
    getPreviewChatBrief(),
    getPreviewChatBrief(message = "See you later alligator")
  )
)