package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import com.lofigroup.seeyau.features.chat_screen.ui.components.ChatItem
import com.lofigroup.seeyau.features.chat_screen.ui.components.ChatList
import com.lofigroup.seeyau.features.chat_screen.ui.components.TopBar
import com.sillyapps.core.ui.components.ShowToast

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChatListScreen(
  stateHolder: ChatListScreenStateHolder,
  onItemClick: (Long) -> Unit,
  onUpButtonClick: () -> Unit,
  initialState: ChatListScreenState = ChatListScreenState()
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = initialState)

  Surface(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      modifier = Modifier.systemBarsPadding()
    ) {
      TopBar(
        totalNewMessages = state.newMessagesCount,
        onUpButtonClick = onUpButtonClick
      )

      ChatList(
        memoryFolder = state.memoryFolder,
        likesFolder = state.likesFolder,
        interactionFolder = state.interactionFolder,
        onItemClick = onItemClick,
        modifier = Modifier.weight(1f)
      )
    }
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }

}

@Preview
@Composable
fun ChatListScreenPreview() {
  val state = MutableStateFlow(previewState)

  val stateHolder = object : ChatListScreenStateHolder {
    override fun getState(): Flow<ChatListScreenState> = state
  }

  AppTheme() {
    Surface() {
      ChatListScreen(
        stateHolder = stateHolder,
        onItemClick = {},
        onUpButtonClick = {},
        initialState = previewState
      )
    }
  }
}

val previewState = ChatListScreenState(
  interactionFolder = listOf(
    ChatBrief(
      id = 1,
      lastMessage = ChatMessage.PlainMessage(id = 0, message = "Hello hero!", author = 0, createdIn = 0L, isRead = true),
      partner = User(
        id = 9,
        name = "York",
        imageUrl = "",
        isNear = true,
        lastConnection = 0,
        isOnline = true
      ),
      newMessagesCount = 1,
      chatDraft = null,
      likedYouAt = 0L
    ),
    ChatBrief(
      id = 1,
      lastMessage = ChatMessage.PlainMessage(id = 0, message = "Hi alligator!", author = 0, createdIn = 0L, isRead = true),
      partner = User(
        id = 9,
        name = "Umbrella",
        imageUrl = "",
        isNear = true,
        lastConnection = 0,
        isOnline = false
      ),
      newMessagesCount = 1,
      chatDraft = null,
      likedYouAt = 0L
    )
  )
)