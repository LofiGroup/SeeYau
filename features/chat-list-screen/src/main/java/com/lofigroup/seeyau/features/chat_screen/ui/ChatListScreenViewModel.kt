package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.lifecycle.ViewModel
import com.lofigroup.seeyau.domain.chat.usecases.ObserveChatsUseCase
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatListScreenViewModel @Inject constructor(
  private val observeChatsUseCase: ObserveChatsUseCase
): ViewModel(), ChatListScreenStateHolder {

  override fun getState(): Flow<ChatListScreenState> = observeChatsUseCase().map { chats ->
    ChatListScreenState(
      chats = chats,
      newMessagesCount = chats.sumOf { it.newMessagesCount }
    )
  }

}