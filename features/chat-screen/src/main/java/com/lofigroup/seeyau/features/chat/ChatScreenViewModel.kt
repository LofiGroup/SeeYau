package com.lofigroup.seeyau.features.chat

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.chat.usecases.GetChatUseCase
import com.lofigroup.seeyau.domain.chat.usecases.MarkChatAsReadUseCase
import com.lofigroup.seeyau.domain.chat.usecases.SendChatMessageUseCase
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.toPrivateMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatScreenViewModel @Inject constructor(
  private val getChatUseCase: GetChatUseCase,
  private val sendChatMessageUseCase: SendChatMessageUseCase,
  private val markChatAsReadUseCase: MarkChatAsReadUseCase,
  private val chatId: Long,
  private val resources: Resources
): ViewModel(), ChatScreenStateHolder {

  init {
    viewModelScope.launch {
      markChatAsReadUseCase(chatId)
      observeChatUpdates()
    }
  }
  private val state = MutableStateFlow(ChatScreenState())

  override fun getChatState(): Flow<ChatScreenState> = state

  override fun sendMessage() {
    val message = state.value.message
    state.apply { value = value.copy(message = "") }

    viewModelScope.launch {
      sendChatMessageUseCase(message, chatId)
    }
  }

  override fun setMessage(message: String) {
    state.apply { value = value.copy(message = message) }
  }

  private suspend fun observeChatUpdates() {
    getChatUseCase(chatId).collect() { chat ->
      state.apply { value = value.copy(
        partner = chat.partner,
        messages = chat.messages.mapIndexed { index, chatMessage ->  chatMessage.toPrivateMessage(index, resources) }
      ) }
      markChatAsReadUseCase(chatId)
    }
  }

}