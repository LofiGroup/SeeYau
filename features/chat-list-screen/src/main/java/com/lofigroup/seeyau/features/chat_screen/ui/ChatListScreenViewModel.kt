package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.lifecycle.ViewModel
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.usecases.ObserveChatsUseCase
import com.lofigroup.seeyau.features.chat_screen.model.*
import com.sillyapps.core_time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatListScreenViewModel @Inject constructor(
  private val observeChatsUseCase: ObserveChatsUseCase
): ViewModel(), ChatListScreenStateHolder {

  override fun getState(): Flow<ChatListScreenState> = observeChatsUseCase().map { chats ->
    val ordered = chats.sortedByDescending { it.lastMessage?.createdIn ?: it.createdIn }

    val memoryFolder = mutableListOf<FolderChat.MemoryChat>()
    val likesFolder = mutableListOf<FolderChat.LikeChat>()

    for (chat in ordered) {
      if (chat.draft.message.isNotBlank()) {
        memoryFolder.add(chat.toMemoryChat())
      }

      val likedYouAt = chat.partner.likedYouAt ?: 0L
      if (System.currentTimeMillis() - likedYouAt < Time.d) {
        likesFolder.add(chat.toLikeChat())
      }
    }

    ChatListScreenState(
      memoryFolder = memoryFolder.sortedByDescending { it.createdIn },
      likesFolder = likesFolder.sortedByDescending { it.likedAt },
      interactionFolder = ordered.map { it.toDefaultChat() },
      newMessagesCount = ordered.sumOf { it.newMessagesCount }
    )
  }

}