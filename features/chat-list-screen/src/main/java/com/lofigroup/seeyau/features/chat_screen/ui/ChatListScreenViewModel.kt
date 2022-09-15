package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.chat.usecases.GetChatsUseCase
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListScreenViewModel @Inject constructor(
  private val getChatsUseCase: GetChatsUseCase
): ViewModel(), ChatListScreenStateHolder {

  init {
  }

  override fun getState(): Flow<ChatListScreenState> = getChatsUseCase().map {
    ChatListScreenState(chats = it)
  }

}