package com.lofigroup.seeyau.features.chat

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.chat.usecases.*
import com.lofigroup.seeyau.domain.profile.usecases.BlacklistUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetUserUseCase
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.toPrivateMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ChatScreenViewModel @Inject constructor(
  private val getUserIdByChatIdUseCase: GetUserIdByChatIdUseCase,
  private val getUserUseCase: GetUserUseCase,

  private val updateChatDraftUseCase: UpdateChatDraftUseCase,

  private val observeChatUseCase: ObserveChatUseCase,
  private val observeChatEventsUseCase: ObserveChatEventsUseCase,

  private val sendChatMessageUseCase: SendChatMessageUseCase,
  private val markChatAsReadUseCase: MarkChatAsReadUseCase,

  private val blacklistUserUseCase: BlacklistUserUseCase,

  private val chatId: Long,
  private val resources: Resources
) : ViewModel(), ChatScreenStateHolder {

  private val state = MutableStateFlow(ChatScreenState())
  private val commands = MutableSharedFlow<ChatScreenCommand>()

  private val job: Job

  init {
    job = viewModelScope.launch {
      coroutineScope {
        launch {
          markChatAsReadUseCase(chatId)
        }
        launch {
          observeProfileUpdates()
        }
        launch {
          observeChatUpdates()
        }
        launch {
          observeChatEvents()
        }
      }
    }
  }

  override fun getChatState(): Flow<ChatScreenState> = state

  override fun getCommands(): Flow<ChatScreenCommand> {
    return commands
  }

  override fun sendMessage() {
    val message = state.value.message
    if (message.isBlank()) {
      return
    }

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
        chatId = chatId
      ))
    }
  }

  override fun onIgnoreUser() {
    job.cancel()
    viewModelScope.launch {
      blacklistUserUseCase(state.value.partner.id)
      commands.emit(ChatScreenCommand.Exit)
    }
  }

  private suspend fun observeProfileUpdates() {
    val profileId = getUserIdByChatIdUseCase(chatId)

    if (profileId == null) {
      Timber.e("ProfileId for chat with id = $chatId is null!")
      return
    }

    getUserUseCase(profileId).collect() { user ->
      state.apply { value = value.copy(partner = user) }
    }
  }

  private suspend fun observeChatUpdates() {
    observeChatUseCase(chatId).collect() { chat ->
      state.apply {
        value = value.copy(
          message = chat.draft.ifBlank { "" },
          messages = chat.messages
            .map { chatMessage -> chatMessage.toPrivateMessage(resources) }
            .groupBy { it.dateTime.date }
        )
      }
    }

  }

  private suspend fun observeChatEvents() {
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