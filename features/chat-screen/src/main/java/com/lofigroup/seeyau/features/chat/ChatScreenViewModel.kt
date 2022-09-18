package com.lofigroup.seeyau.features.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.chat.usecases.GetChatUseCase
import com.lofigroup.seeyau.domain.chat.usecases.MarkChatAsReadUseCase
import com.lofigroup.seeyau.domain.chat.usecases.SendChatMessageUseCase
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.toChatScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatScreenViewModel @Inject constructor(
  private val getChatUseCase: GetChatUseCase,
  private val sendChatMessageUseCase: SendChatMessageUseCase,
  private val markChatAsReadUseCase: MarkChatAsReadUseCase,
  private val chatId: Long
): ViewModel(), ChatScreenStateHolder {

  init {
    viewModelScope.launch { markChatAsReadUseCase(chatId) }
  }

  private val mMessage = MutableStateFlow("")

  override fun getChatState(): Flow<ChatScreenState> = getChatUseCase(chatId).map { it.toChatScreenState() }

  override fun getMessage(): Flow<String> = mMessage

  override fun setMessage(message: String) {
    mMessage.value = message
  }

  override fun sendMessage() {
    viewModelScope.launch {
      sendChatMessageUseCase(mMessage.value, chatId)
    }
    mMessage.value = ""
  }

}