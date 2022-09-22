package com.lofigroup.features.navigator_screen.ui

import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NavigatorScreenStateHolder {

  fun getState(): Flow<NavigatorScreenState>

  fun selectUser(id: Long)

  fun onToggleFullScreenMode()

  fun onIgnoreSelectedUser()

  fun onShowChat()

}

val fakeStateHolder = object : NavigatorScreenStateHolder {
  override fun getState(): Flow<NavigatorScreenState> {
    return flow {
      emit(
        previewModel
      )
    }
  }

  override fun selectUser(id: Long) {

  }

  override fun onToggleFullScreenMode() {

  }

  override fun onIgnoreSelectedUser() {

  }

  override fun onShowChat() {

  }

}

val previewModel = NavigatorScreenState(
  metUsers = listOf(
    UserItemUIModel.getPreviewModel(),
    UserItemUIModel.getPreviewModel(),
    UserItemUIModel.getPreviewModel()
  ),
  newMessagesCount = 1
)