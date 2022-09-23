package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import com.lofigroup.seeyau.features.chat_screen.ui.components.ChatItem
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChatListScreen(
  stateHolder: ChatListScreenStateHolder,
  onItemClick: (Long) -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = ChatListScreenState())

  Box() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(items = state.chats) {
        ChatItem(
          chat = it,
          onClick = onItemClick
        )
      }
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
  val state = MutableStateFlow(
    ChatListScreenState(
      chats = listOf(
        Chat(
          id = 1,
          messages = listOf(),
          partner = User(
            id = 9,
            name = "York",
            imageUrl = "",
            isNear = true,
            lastConnection = 0
          )
        ),
        Chat(
          id = 1,
          messages = listOf(),
          partner = User(
            id = 9,
            name = "Umbrella",
            imageUrl = "",
            isNear = true,
            lastConnection = 0
          )
        )
      )
    )
  )

  val stateHolder = object : ChatListScreenStateHolder {
    override fun getState(): Flow<ChatListScreenState> = state
  }

  AppTheme() {
    Surface() {
      ChatListScreen(
        stateHolder = stateHolder,
        onItemClick = {}
      )
    }
  }
}