package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.lifecycle.ViewModel
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.usecases.ObserveChatsUseCase
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import com.sillyapps.core_time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatListScreenViewModel @Inject constructor(
  private val observeChatsUseCase: ObserveChatsUseCase
): ViewModel(), ChatListScreenStateHolder {

  override fun getState(): Flow<ChatListScreenState> = observeChatsUseCase().map { chats ->
    val memoryFolder = mutableListOf<ChatBrief>()
    val likesFolder = mutableListOf<ChatBrief>()
    val interactionFolder = mutableListOf<ChatBrief>()

    for (chat in chats) {
      if (chat.draft.isNotBlank()) {
        memoryFolder.add(chat)
        continue
      }

      val likedYouAt = chat.partner.likedYouAt ?: 0L
      if (System.currentTimeMillis() - likedYouAt < Time.d) {
        likesFolder.add(chat)
        continue
      }
      interactionFolder.add(chat)
    }

    ChatListScreenState(
      memoryFolder = memoryFolder,
      likesFolder = likesFolder,
      interactionFolder = interactionFolder,
      newMessagesCount = chats.sumOf { it.newMessagesCount }
    )
  }

}