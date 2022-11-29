package com.lofigroup.seeyau.features.chat

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.components.specific.BigImage
import com.lofigroup.seayau.common.ui.components.specific.UserOptionsDialog
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.features.chat.components.*
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.media_player.test.FakeMediaPlayer
import com.lofigroup.seeyau.features.chat.util.LocalPlayerProvider
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer
import com.lofigroup.seeyau.features.send_media.SendMediaDialog
import com.sillyapps.core.ui.components.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

@Composable
fun ChatScreen(
  stateHolder: ChatScreenStateHolder,
  onUpButtonClick: () -> Unit,
  initialState: ChatScreenState = ChatScreenState()
) {

  val state by remember(stateHolder) {
    stateHolder.getChatState()
  }.collectAsState(initial = initialState)

  val context = LocalContext.current
  val listState = rememberLazyListState()

  var optionsDialogVisible by rememberSaveable {
    mutableStateOf(false)
  }

  var bigImageVisible by rememberSaveable {
    mutableStateOf(false)
  }

  var sendMediaVisible by rememberSaveable() {
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

  Column(
    modifier = Modifier
      .fillMaxSize()
      .systemBarsPadding()
      .imePadding()
  ) {
    TopBar(
      partner = state.partner,
      onUpButtonClick = onUpButtonClick,
      onMoreButtonClick = { optionsDialogVisible = true },
      onUserIconClick = { bigImageVisible = true }
    )

    LocalPlayerProvider(mediaPlayer = stateHolder.getMediaPlayer()) {
      ChatMessages(
        items = state.messages,
        listState = listState
      )
    }

    MessageInput(
      message = state.message,
      setMessage = stateHolder::setMessage,
      sendMessage = stateHolder::sendMessage,
      onClipButtonClick = { sendMediaVisible = true }
    )
  }

  BackHandler {
    stateHolder.onExit()
    onUpButtonClick()
  }

  UserOptionsDialog(
    isVisible = optionsDialogVisible,
    setVisible = { optionsDialogVisible = it },
    onWriteMessage = {},
    onIgnoreUser = {
      optionsDialogVisible = false
      stateHolder.onIgnoreUser()
    },
    onNavigateToChatOptionEnabled = false
  )

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

    override fun sendMessage(mediaUri: Uri?) {

    }

    override fun setMessage(message: String) {
      mMessage.value = message
    }

    override fun getMediaPlayer(): MediaPlayer {
      return FakeMediaPlayer()
    }

    override fun onExit() {

    }

    override fun onIgnoreUser() {

    }
  }
  
  AppTheme() {
    Surface() {
      ChatScreen(
        stateHolder = stateHolder,
        onUpButtonClick = {},
        initialState = getPreviewModel()
      )
    }
  }
}

private fun getPreviewModel(): ChatScreenState {
  return ChatScreenState(
    partner = getUserPreviewModel(),
    messages = listOf(
      getPreviewPrivateMessage(),
      getPreviewPrivateMessage(authorIsMe = false),
      getPreviewPrivateMessage(message = "Very very very very very very very very very very long message.")
    ).groupBy { it.dateTime.date }
  )
}