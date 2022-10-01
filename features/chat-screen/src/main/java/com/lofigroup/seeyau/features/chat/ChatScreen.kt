package com.lofigroup.seeyau.features.chat

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.getLocalizedLastSeen
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat.components.ChatMessages
import com.lofigroup.seeyau.features.chat.components.MessageInput
import com.lofigroup.seeyau.features.chat.components.TopBar
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChatScreen(
  stateHolder: ChatScreenStateHolder,
  onUpButtonClick: () -> Unit,
  initialState: ChatScreenState = ChatScreenState()
) {

  val state by remember(stateHolder) {
    stateHolder.getChatState()
  }.collectAsState(initial = initialState)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .systemBarsPadding()
      .imePadding()
  ) {
    TopBar(
      partner = state.partner,
      onUpButtonClick = onUpButtonClick
    )

    ChatMessages(
      items = state.messages
    )

    MessageInput(
      message = state.message,
      setMessage = stateHolder::setMessage,
      sendMessage = stateHolder::sendMessage
    )
  }

  BackHandler {
    onUpButtonClick()
  }
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

    override fun setMessage(message: String) {
      mMessage.value = message
    }

    override fun sendMessage() {

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
    partner = User(0, "Roman", imageUrl = "", lastConnection = 10000, isNear = false),
    messages = listOf(
      getPreviewPrivateMessage(),
      getPreviewPrivateMessage(authorIsMe = false),
      getPreviewPrivateMessage(message = "Very very very very very very very very very very long message.")
    )
  )
}