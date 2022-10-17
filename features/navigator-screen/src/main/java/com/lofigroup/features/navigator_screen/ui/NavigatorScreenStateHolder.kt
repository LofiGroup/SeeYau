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

  fun onSetLike(like: Boolean, userId: Long)

  suspend fun getChatIdByUserId(userId: Long): Long?

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

  override fun onSetLike(like: Boolean, userId: Long) {

  }

  override suspend fun getChatIdByUserId(userId: Long): Long {
    return 0
  }

}

val previewModel = NavigatorScreenState(
  sortedUsers = listOf(
    UserItemUIModel.getPreviewModel(),
    UserItemUIModel.getPreviewModel(),
    UserItemUIModel.getPreviewModel()
  ),
  splitIndex = 1,
  newMessagesCount = 1
)