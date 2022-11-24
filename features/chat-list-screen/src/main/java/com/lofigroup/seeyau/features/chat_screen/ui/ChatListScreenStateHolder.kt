package com.lofigroup.seeyau.features.chat_screen.ui

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import com.lofigroup.seeyau.features.chat_screen.model.FolderChat
import kotlinx.coroutines.flow.Flow

interface ChatListScreenStateHolder {

  fun getState(): Flow<ChatListScreenState>

}

private fun getPreviewChatBrief(message: String = "Hello"): FolderChat.DefaultChat {
  return FolderChat.DefaultChat(
    id = 1,
    lastMessage = ChatMessage(id = 0, message = message, author = 0, createdIn = 0L, status = MessageStatus.READ, mediaUri = null, type = MessageType.PLAIN),
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