package com.lofigroup.seeyau.features.chat

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.domain.navigator.model.User
import com.lofigroup.seayau.common.ui.getLocalizedLastSeen
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.sillyapps.core.ui.components.RemoteImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChatScreen(
  stateHolder: ChatScreenStateHolder
) {

  val state by remember(stateHolder) {
    stateHolder.getChatState()
  }.collectAsState(initial = getPreviewModel())

  val message by remember(stateHolder) {
    stateHolder.getMessage()
  }.collectAsState(initial = "")

  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      RemoteImage(
        model = state.partner.imageUrl,
        modifier = Modifier
          .size(40.dp)
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(start = 4.dp)
      ) {
        Text(
          text = state.partner.name,
          style = MaterialTheme.typography.h5
        )
        Text(
          text = getLastSeen(
            millis = state.partner.lastConnection,
            context = LocalContext.current
          )
        )
      }
    }

    Divider()
    
    LazyColumn(
      reverseLayout = true,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),

    ) {
      items(items = state.messages) { message ->
        Box(modifier = Modifier.fillMaxWidth()) {
          ChatMessageItem(
            chatMessage = message
          )
        }
      }
    }
    Box(
      modifier = Modifier.padding(8.dp)
    ) {
      TextField(
        value = message,
        onValueChange = stateHolder::setMessage,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Next,
          keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            stateHolder.sendMessage()
          }
        )
      )
      IconButton(
        onClick = stateHolder::sendMessage,
        modifier = Modifier.align(Alignment.CenterEnd)
      ) {
        Icon(imageVector = Icons.Filled.Send, contentDescription = null)
      }
    }
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

    override fun getMessage(): Flow<String> {
      return mMessage
    }

    override fun setMessage(message: String) {
      mMessage.value = message
    }

    override fun sendMessage() {

    }
  }
  
  AppTheme() {
    Surface() {
      ChatScreen(stateHolder = stateHolder)
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