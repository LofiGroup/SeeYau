package com.lofigroup.features.navigator_screen.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.transformItemAt
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.domain.navigator.usecases.GetNearbyUsersUseCase
import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.model.toUIModel
import com.lofigroup.seeyau.domain.chat.usecases.GetChatIdByUserIdUseCase
import com.lofigroup.seeyau.domain.profile.usecases.BlackListUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetProfileUseCase
import com.lofigroup.seeyau.domain.profile.usecases.LikeUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.UnLikeUserUseCase
import com.sillyapps.core_time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavigatorScreenViewModel @Inject constructor(
  private val getNearbyUsersUseCase: GetNearbyUsersUseCase,
  private val getChatIdByUserIdUseCase: GetChatIdByUserIdUseCase,
  private val getProfileUseCase: GetProfileUseCase,
  private val likeUserUseCase: LikeUserUseCase,
  private val unLikeUserUseCase: UnLikeUserUseCase,
  private val blackListUserUseCase: BlackListUserUseCase,
  private val resources: Resources
) : ViewModel(), NavigatorScreenStateHolder {

  private val state = MutableStateFlow(NavigatorScreenState())

  init {
    observeProfile()
    observeNearbyUsers()
  }

  override fun getState(): Flow<NavigatorScreenState> {
    return state
  }

  override fun selectUser(id: Long) {
    state.apply {
      val pos = value.sortedUsers.indexOfFirst { it.id == id }

      value = if (value.selectedUserPos != pos) {
        value.copy(
          sortedUsers = value.sortedUsers.mapIndexed() { index, nearbyUser ->
            nearbyUser.copy(isSelected = index == pos)
          },
          selectedUserPos = pos
        )
      } else {
        value.copy(
          sortedUsers = value.sortedUsers.transformItemAt(pos) {
            it.copy(isSelected = false)
          },
          selectedUserPos = NavigatorScreenState.NO_USER_SELECTED
        )
      }

    }
  }

  override fun onToggleFullScreenMode() {
    state.apply { value = value.copy(fullScreenMode = !value.fullScreenMode) }
  }

  override fun onIgnoreSelectedUser() {
    val selectedUserId = state.value.selectedUser?.id ?: return
    viewModelScope.launch {
      blackListUserUseCase(selectedUserId)
    }
  }

  override fun onShowChat() {
    state.apply {
      value = value.copy(
        sortedUsers = value.sortedUsers.transformItemAt(value.selectedUserPos) {
          it.copy(messagesIsCollapsed = true)
        }
      )
    }
  }

  override fun onSetLike(like: Boolean, userId: Long) {
    viewModelScope.launch {
      if (like) likeUserUseCase(userId)
      else unLikeUserUseCase(userId)
    }
  }

  override suspend fun getChatIdByUserId(userId: Long): Long? {
    return getChatIdByUserIdUseCase(userId)
  }

  private fun observeProfile() {
    viewModelScope.launch {
      getProfileUseCase().collect { profile ->
        state.value = state.value.copy(profile = profile)
      }
    }
  }

  private fun observeNearbyUsers() {
    viewModelScope.launch {
      getNearbyUsersUseCase().collect { users ->
        applyUpdates(users)
      }
    }
  }

  private fun applyUpdates(nearbyUsers: List<NearbyUser>) {
    val sorted = nearbyUsers.sortedByDescending {
      it.lastContact
    }

    val splitIndex = sorted.indexOfFirst {
      it.lastContact > System.currentTimeMillis() - 1 * Time.m
    } + 1

    val selectedUserPos = sorted.indexOfFirst { it.id == state.value.selectedUser?.id }

    val sortedMapped = sorted.mapIndexed { index, nearbyUser ->
      nearbyUser.toUIModel(
        isSelected = index == selectedUserPos,
        oldValue = state.value.sortedUsers.firstOrNull() { it.id == nearbyUser.id },
        resources = resources
      )
    }

    state.value = state.value.copy(
      selectedUserPos = selectedUserPos,
      sortedUsers = sortedMapped,
      newMessagesCount = sortedMapped.sumOf { it.newMessages.size },
      splitIndex = splitIndex
    )
  }
}
