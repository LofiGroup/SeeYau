package com.lofigroup.seeyau.features.chat

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.getLocalizedLastSeen
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.components.ChatMessages
import com.lofigroup.seeyau.features.chat.components.MessageInput
import com.lofigroup.seeyau.features.chat.components.TopBar
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.applyActivityBarPaddings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ChatScreen(
  stateHolder: ChatScreenStateHolder,
  onUpButtonClick: () -> Unit,
  initialState: ChatScreenState = ChatScreenState()
) {

  val state by remember(stateHolder) {
    stateHolder.getChatState()
  }.collectAsState(initial = initialState)

  Column(modifier = Modifier.applyActivityBarPaddings()) {
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

fun getLastSeen(millis: Long, context: Context): String {
  val lastSeen = getLocalizedLastSeen(millis, context)
  return "${context.getString(R.string.last_seen_in)} $lastSeen"
}

private fun getPreviewModel(): ChatScreenState {
  return ChatScreenState(
    partner = User(0, "Roman", imageUrl = "", lastConnection = 10000, isNear = false),
    messages = listOf(
      PrivateMessage(
        id = 0,
        message = "Hello!",
        authorIsMe = true,
        createdIn = 0L,
        isRead = true
      ),
      PrivateMessage(
        id = 0,
        message = "Hello!",
        authorIsMe = false,
        createdIn = 1L,
        isRead = true
      ),
      PrivateMessage(
        id = 0,
        message = "Very very very very very very very very very very long message.",
        authorIsMe = true,
        createdIn = 2L,
        isRead = true
      ),
    )
  )
}