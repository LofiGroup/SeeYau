package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.components.specific.BigImage
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
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

  var bigImageVisible by rememberSaveable {
    mutableStateOf(false)
  }
  var bigImageUrl by rememberSaveable() {
    mutableStateOf<String?>(null)
  }

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
        onIconClick = {
          bigImageUrl = it
          bigImageVisible = true
        },
        modifier = Modifier.weight(1f)
      )
    }
  }

  BigImage(
    isVisible = bigImageVisible,
    onDismiss = { bigImageVisible = false },
    url = bigImageUrl
  )

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

