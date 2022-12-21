package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.R
import com.lofigroup.seeyau.common.ui.components.YesNoChoiceDialog
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import com.lofigroup.seeyau.features.chat_screen.ui.components.ChatList
import com.lofigroup.seeyau.features.chat_screen.ui.components.CloseUserProfileDialog
import com.lofigroup.seeyau.features.chat_screen.ui.components.TopBar
import com.sillyapps.core.ui.theme.LocalExtendedColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChatListScreen(
  stateHolder: ChatListScreenStateHolder,
  onNavigateToChatScreen: (Long) -> Unit,
  onNavigateToSettingsScreen: () -> Unit,
  initialState: ChatListScreenState = ChatListScreenState()
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = initialState)

  var deleteUserDialogVisible by rememberSaveable() {
    mutableStateOf(false)
  }
  var userToBlacklist by rememberSaveable() {
    mutableStateOf(0L)
  }

  Surface(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      modifier = Modifier.systemBarsPadding()
    ) {
      TopBar(
        profile = state.profile,
        onProfileButtonClick = onNavigateToSettingsScreen
      )

      ChatList(
        nearby = state.nearbyFolder,
        metToday = state.metFolder,
        chats = state.interactionFolder,
        isVisible = state.isVisible,
        onItemClick = onNavigateToChatScreen,
        onGridItemClick = stateHolder::setCurrentChat,
        modifier = Modifier.weight(1f),
        onDeleteChat = {
          deleteUserDialogVisible = true
          userToBlacklist = it
        }
      )
    }
  }

  CloseUserProfileDialog(
    chat = state.currentItem,
    likeUser = stateHolder::likeUser,
    onDismiss = { stateHolder.setCurrentChat(null) },
    onChatButtonClick = onNavigateToChatScreen
  )

  YesNoChoiceDialog(
    visible = deleteUserDialogVisible,
    onConfirm = { stateHolder.blacklistUser(userToBlacklist) },
    onDismiss = { deleteUserDialogVisible = false },
    title = stringResource(id = R.string.are_you_sure),
    details = stringResource(id = R.string.ignore_user_detail)
  )

}

@Preview
@Composable
fun ChatListScreenPreview() {
  val state = MutableStateFlow(previewState)

  val stateHolder = object : ChatListScreenStateHolder {
    override fun getState(): Flow<ChatListScreenState> = state
    override fun blacklistUser(userId: Long) {

    }

    override fun setCurrentChat(chatBrief: ChatBrief?) {

    }

    override fun likeUser(isLiked: Boolean, userId: Long) {

    }
  }

  AppTheme() {
    Surface() {
      ChatListScreen(
        stateHolder = stateHolder,
        onNavigateToChatScreen = {},
        onNavigateToSettingsScreen = {},
        initialState = previewState
      )
    }
  }
}

