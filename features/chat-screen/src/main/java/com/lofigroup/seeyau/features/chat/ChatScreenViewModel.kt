package com.lofigroup.seeyau.features.chat

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.chat.usecases.*
import com.lofigroup.seeyau.domain.profile.usecases.GetUserUseCase
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.toPrivateMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ChatScreenViewModel @Inject constructor(
  private val getUserIdByChatIdUseCase: GetUserIdByChatIdUseCase,
  private val getUserUseCase: GetUserUseCase,

  private val getChatDraftUseCase: GetChatDraftUseCase,
  private val updateChatDraftUseCase: UpdateChatDraftUseCase,

  private val observeChatUseCase: ObserveChatUseCase,
  private val observeChatEventsUseCase: ObserveChatEventsUseCase,

  private val sendChatMessageUseCase: SendChatMessageUseCase,
  private val markChatAsReadUseCase: MarkChatAsReadUseCase,

  private val chatId: Long,
  private val resources: Resources
) : ViewModel(), ChatScreenStateHolder {

  private val state = MutableStateFlow(ChatScreenState())
  private val commands = MutableSharedFlow<ChatScreenCommand>()

  init {
    viewModelScope.launch {
      markChatAsReadUseCase(chatId)
      setMessageFromDraft()
    }
    observeProfileUpdates()
    observeChatUpdates()
    observeChatEvents()
  }

  override fun getChatState(): Flow<ChatScreenState> = state

  override fun getCommands(): Flow<ChatScreenCommand> {
    return commands
  }

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

  override fun onExit() {
    viewModelScope.launch {
      updateChatDraftUseCase(ChatDraft(
        message = state.value.message,
        createdIn = System.currentTimeMillis(),
        chatId = chatId
      ))
    }
  }

  private suspend fun setMessageFromDraft() {
    val draft = getChatDraftUseCase(chatId) ?: return
    setMessage(draft.message)
  }

  private fun observeProfileUpdates() {
    viewModelScope.launch {
      val profileId = getUserIdByChatIdUseCase(chatId)

      if (profileId == null) {
        Timber.e("ProfileId for chat with id = $chatId is null!")
        return@launch
      }

      getUserUseCase(profileId).collect() { user ->
        state.apply { value = value.copy(partner = user) }
      }
    }
  }

  private fun observeChatUpdates() {
    viewModelScope.launch {
      observeChatUseCase(chatId).collect() { chat ->
        state.apply {
          value = value.copy(
            messages = chat.messages
              .map { chatMessage -> chatMessage.toPrivateMessage(resources) }
              .groupBy { it.dateTime.date }
          )
        }
      }
    }
  }

  private fun observeChatEvents() {
    viewModelScope.launch {
      observeChatEventsUseCase(chatId).collect() {
        when (it) {
          is ChatIsRead -> {

          }
          is NewChatMessage -> {
            markChatAsReadUseCase(chatId)
            commands.emit(ChatScreenCommand.ToLatestMessage)
          }
        }
      }
    }
  }

}