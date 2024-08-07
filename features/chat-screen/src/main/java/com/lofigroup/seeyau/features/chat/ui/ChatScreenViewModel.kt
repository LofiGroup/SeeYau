package com.lofigroup.seeyau.features.chat.ui

import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.audio_recorder.AudioRecorder
import com.lofigroup.core.util.set
import com.lofigroup.seeyau.domain.chat.models.ChatDraftUpdate
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.chat.usecases.*
import com.lofigroup.seeyau.domain.profile.usecases.BlacklistUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetUserUseCase
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.toUIMessage
import kotlinx.coroutines.Job
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

  private val getChatUseCase: GetChatUseCase,
  private val observeChatMessages: ObserveChatMessages,
  private val observeChatEventsUseCase: ObserveChatEventsUseCase,

  private val sendChatMessageUseCase: SendChatMessageUseCase,
  private val markChatAsReadUseCase: MarkChatAsReadUseCase,

  private val blacklistUserUseCase: BlacklistUserUseCase,

  private val resources: Resources,

  private val mediaPlayer: MediaPlayer,

  private val audioRecorder: AudioRecorder
) : ViewModel(), ChatScreenStateHolder {

  private val state = MutableStateFlow(ChatScreenState())
  private val commands = MutableSharedFlow<ChatScreenCommand>()

  private var observeChatJob: Job? = null

  private var currentChatId = -1L

  init {
    Timber.e("New chat screen viewModel is created")
  }

  fun setChatId(chatId: Long) {
    if (chatId == 0L || chatId == currentChatId) return
    observeChatJob?.cancel()

    currentChatId = chatId
    audioRecorder.deleteRecording()

    observeChatJob = viewModelScope.launch {
      launch { observeProfileUpdates(chatId) }

      launch {
        val chat = getChatUseCase(chatId)
        state.apply { value = value.copy(message = chat.draft) }
      }

      launch { markChatAsReadUseCase(chatId) }

      launch { observeMessages(chatId) }
      launch { observeChatEvents(chatId) }
    }
  }

  override fun getChatState(): Flow<ChatScreenState> = state

  override fun getCommands(): Flow<ChatScreenCommand> {
    return commands
  }

  override fun sendMessage(mediaUri: Uri?) {
    val message = state.value.message
    if (message.isBlank() && mediaUri == null) {
      return
    }

    state.apply { value = value.copy(message = "") }

    viewModelScope.launch {
      sendChatMessageUseCase(ChatMessageRequest(message, currentChatId, mediaUri = mediaUri.toString()))
    }
  }


  override fun setMessage(message: String) {
    state.apply { value = value.copy(message = message) }
  }

  override fun getMediaPlayer() = mediaPlayer

  override fun onExit() {
    viewModelScope.launch {
      updateChatDraftUseCase(ChatDraftUpdate(
        message = state.value.message,
        chatId = currentChatId
      ))
    }
  }

  override fun onIgnoreUser() {
    observeChatJob?.cancel()
    viewModelScope.launch {
      blacklistUserUseCase(state.value.partner.id)
      commands.emit(ChatScreenCommand.Exit)
    }
  }

  override fun getRecorder(): AudioRecorder {
    return audioRecorder
  }

  override fun setCloseUpImage(imageUri: String?) {
    state.set {
      it.copy(currentCloseUpImage = imageUri)
    }
  }

  override fun setCloseUpVideo(video: UIMessageType.Video) {
    state.set {
      it.copy(currentCloseUpVideo = video)
    }
  }

  private suspend fun observeProfileUpdates(chatId: Long) {
    val profileId = getUserIdByChatIdUseCase(chatId)

    if (profileId == null) {
      Timber.e("ProfileId for chat with id = $chatId is null!")
      return
    }

    getUserUseCase(profileId).collect() { user ->
      state.apply { value = value.copy(partner = user) }
    }
  }

  private suspend fun observeMessages(chatId: Long) {
    observeChatMessages(chatId).collect() { messages ->
      state.apply {
        value = value.copy(
          messages = messages
            .mapIndexed { pos, chatMessage -> chatMessage.toUIMessage(resources, pos) }
            .groupBy { it.dateTime.date }
        )
      }
    }
  }

  private suspend fun observeChatEvents(chatId: Long) {
    observeChatEventsUseCase().collect() {
      if (it.chatId != chatId) return@collect

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


  override fun onCleared() {
    mediaPlayer.destroy()
    audioRecorder.release()
    super.onCleared()
  }

}