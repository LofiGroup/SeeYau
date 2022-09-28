package com.lofigroup.features.navigator_screen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.ui.components.*
import com.lofigroup.seayau.common.ui.components.OptionsDialog
import com.lofigroup.seayau.common.ui.components.OptionsDialogItem
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.ShowToast
import com.sillyapps.core.ui.theme.applyActivityBarPaddings

@Composable
fun NavigatorScreen(
  stateHolder: NavigatorScreenStateHolder,
  onNavigateToChat: (Long) -> Unit,
  onNavigateToSettings: () -> Unit,
  onNavigateToChatList: () -> Unit
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = previewModel)

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
          modifier = Modifier.applyActivityBarPaddings()
        ) {
          TopBar(
            newMessagesCount = state.newMessagesCount,
            onSettingsButtonClick = onNavigateToSettings,
            onCloudButtonClick = onNavigateToChatList
          )
          val selectedUser = state.selectedUser
          if (selectedUser != null) {
            UserScreen(
              user = selectedUser,
              isInFullScreenMode = state.fullScreenMode,
              onToggleFullScreenMode = stateHolder::onToggleFullScreenMode,
              onMoreButtonClicked = {
                optionsDialogVisible = true
              },
              onShowChat = stateHolder::onShowChat,
              onGoToChat = onNavigateToChat
            )
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
    onDismiss = { optionsDialogVisible = false }
  ) {
    OptionsDialogItem(
      text = stringResource(id = R.string.write_message),
      textColor = MaterialTheme.colors.secondary
    )
    OptionsDialogItem(
      text = stringResource(id = R.string.ignore_user)
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
      onNavigateToSettings = {}
    )
  }
}