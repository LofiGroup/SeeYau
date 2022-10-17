package com.lofigroup.features.navigator_screen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.ui.components.*
import com.lofigroup.seayau.common.ui.components.OptionsDialog
import com.lofigroup.seayau.common.ui.components.OptionsDialogItem
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.launch
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun NavigatorScreen(
  stateHolder: NavigatorScreenStateHolder,
  onNavigateToChat: (Long) -> Unit,
  onNavigateToSettings: () -> Unit,
  onNavigateToChatList: () -> Unit,
  initialState: NavigatorScreenState = NavigatorScreenState()
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = initialState)

  val scope = rememberCoroutineScope()

  var optionsDialogVisible by rememberSaveable {
    mutableStateOf(false)
  }

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Box() {
      BackgroundImage(
        selectedUser = state.selectedUser,
        isInFullScreenMode = state.fullScreenMode
      )

      if (!optionsDialogVisible) {
        Column(
          modifier = Modifier.systemBarsPadding()
        ) {
          TopBar(
            newMessagesCount = state.newMessagesCount,
            profile = state.profile,
            onSettingsButtonClick = onNavigateToSettings,
            onCloudButtonClick = onNavigateToChatList
          )
          val selectedUser = state.selectedUser
          if (selectedUser != null) {
            Box(modifier = Modifier
              .weight(1f)
              .fillMaxWidth()
            ) {
              UserNewMessages(
                selectedUser = selectedUser,
                onClick = {
                  scope.launch {
                    val chatId = stateHolder.getChatIdByUserId(it) ?: return@launch
                    onNavigateToChat(chatId)
                  }
                },
                showChat = stateHolder::onShowChat,
                modifier = Modifier.align(Alignment.BottomStart)
              )
              UserScreenControls(
                user = selectedUser,
                isInFullScreenMode = state.fullScreenMode,
                onToggleFullScreenMode = stateHolder::onToggleFullScreenMode,
                onMoreButtonClicked = { optionsDialogVisible = true },
                onGoToChat = {
                  scope.launch {
                    val chatId = stateHolder.getChatIdByUserId(it) ?: return@launch
                    onNavigateToChat(chatId)
                  }
                },
                onLikeButtonClick = stateHolder::onSetLike,
                modifier = Modifier.align(Alignment.BottomEnd),
              )
            }
          } else {
            DefaultScreen()
          }

          UsersList(
            users = state.sortedUsers,
            splitIndex = state.splitIndex,
            onUserSelected = stateHolder::selectUser
          )
        }
      }
    }
  }

  OptionsDialog(
    visible = optionsDialogVisible,
    onDismiss = { optionsDialogVisible = false },
    caption = stringResource(id = CommonR.string.what_do_you_want_to_do)
  ) {
    OptionsDialogItem(
      text = stringResource(id = CommonR.string.write_message),
      onClick = {
        state.selectedUser?.let {
          scope.launch {
            val chatId = stateHolder.getChatIdByUserId(it.id) ?: return@launch
            onNavigateToChat(chatId)
          }
          optionsDialogVisible = false
        }
      }
    )
    OptionsDialogItem(
      text = stringResource(id = CommonR.string.ignore_user),
      onClick = {
        optionsDialogVisible = false
      }
    )
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }
}

@Preview
@Composable
fun NavigatorScreenPreview() {
  AppTheme {
    NavigatorScreen(
      stateHolder = fakeStateHolder,
      onNavigateToChat = {},
      onNavigateToChatList = {},
      onNavigateToSettings = {},
      initialState = previewModel
    )
  }
}