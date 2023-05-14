package com.lofigroup.seeyau.features.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.core.audio_recorder.AudioRecorder
import com.lofigroup.core.audio_recorder.FakeAudioRecorder
import com.lofigroup.seeyau.common.ui.components.specific.BigImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.features.chat.R
import com.lofigroup.seeyau.features.chat.media_player.FakeMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.*
import com.lofigroup.seeyau.features.chat.ui.composition_locals.*
import com.lofigroup.seeyau.features.send_media.SendMediaDialog
import com.sillyapps.core.ui.components.showToast
import com.sillyapps.core.ui.theme.LocalExtendedColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun ChatScreen(
  stateHolder: ChatScreenStateHolder,
  onUpButtonClick: () -> Unit,
  isFocused: Boolean,
  initialState: ChatScreenState = ChatScreenState(),
) {

  val state by remember(stateHolder) {
    stateHolder.getChatState()
  }.collectAsState(initial = initialState)

  val context = LocalContext.current
  val listState = rememberLazyListState()

  var bigImageVisible by rememberSaveable {
    mutableStateOf(false)
  }

  var sendMediaVisible by rememberSaveable() {
    mutableStateOf(false)
  }

  var closeUpImageVisible by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = Unit) {
    stateHolder.getCommands().collect {
      when (it) {
        is ChatScreenCommand.ShowToast -> {
          showToast(context, it.message)
        }
        ChatScreenCommand.ToLatestMessage -> {
          delay(300)
          listState.animateScrollToItem(0)
        }
        ChatScreenCommand.Exit -> {
          onUpButtonClick()
        }
      }
    }
  }

  CompositionLocalProvider(
    LocalMediaPlayer provides stateHolder.getMediaPlayer(),
    LocalChatMessageStyles provides ChatMessageStyles(
      myMessageStyleDefault.copy(color = MaterialTheme.colors.primaryVariant),
      partnerMessageStyleDefault.copy(color = LocalExtendedColors.current.itemsColors)
    ),
    LocalChatMediaDownloader provides provideChatMediaDownloader(
      fileDownloader = LocalFileDownloader.current,
      startDownload = stateHolder::startMediaDownload
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .imePadding()
    ) {
      TopBar(
        partner = state.partner,
        onUpButtonClick = onUpButtonClick,
        onUserIconClick = { bigImageVisible = true }
      )

      Box(
        modifier = Modifier.weight(1f)
      ) {
        ChatMessages(
          items = state.messages,
          listState = listState,
          onImageClick = {
            stateHolder.setCloseUpImage(it)
            closeUpImageVisible = true
          },
          onVideoClick = {
            showToast(context, context.getString(CommonR.string.not_implemented_yet))
          }
        )

        if (state.messages.isEmpty()) {
          SayHelloButton(
            onSayHelloButtonClick = {
              stateHolder.setMessage(context.getString(R.string.hello))
              stateHolder.sendMessage()
            }
          )
        }
      }

      MessageInput(
        message = state.message,
        setMessage = stateHolder::setMessage,
        sendMessage = stateHolder::sendMessage,
        onPickMedia = { sendMediaVisible = true },
        audioRecorder = stateHolder.getRecorder()
      )
    }
  }

  CloseUpImage(
    imageUri = state.currentCloseUpImage,
    visible = closeUpImageVisible,
    onDismiss = { closeUpImageVisible = false }
  )

  if (isFocused)
    BackHandler {
      stateHolder.onExit()
      onUpButtonClick()
    }

  BigImage(
    isVisible = bigImageVisible,
    onDismiss = { bigImageVisible = false },
    url = state.partner.imageUrl
  )

  SendMediaDialog(
    isVisible = sendMediaVisible,
    onDismiss = { sendMediaVisible = false },
    modifier = Modifier,
    onSendMessage = stateHolder::sendMessage
  )
}

@Preview
@Composable
fun ChatScreenPreview() {
  val stateHolder = object : ChatScreenStateHolder {
    val mState = MutableStateFlow(getPreviewModel())
    val mMessage = MutableStateFlow("")
    
    override fun getChatState(): Flow<ChatScreenState> {
      return mState
    }

    override fun getCommands(): Flow<ChatScreenCommand> {
      return flow {  }
    }

    override fun setMessage(message: String) {
      mMessage.value = message
    }

    override fun getMediaPlayer() = FakeMediaPlayer

    override fun getRecorder(): AudioRecorder = FakeAudioRecorder
  }
  
  AppTheme() {
    Surface() {
      ChatScreen(
        stateHolder = stateHolder,
        onUpButtonClick = {},
        initialState = getPreviewModel(),
        isFocused = true
      )
    }
  }
}

private fun getPreviewModel(): ChatScreenState {
  return ChatScreenState(
    partner = getUserPreviewModel(),
    messages = listOf(
      getPreviewMessage(pos = 0),
      getPreviewMessage(authorIsMe = false, pos = 1),
      getPreviewMessage(message = "Very very very very very very very very very very long message.", pos = 2)
    ).groupBy { it.dateTime.date }
  )
}