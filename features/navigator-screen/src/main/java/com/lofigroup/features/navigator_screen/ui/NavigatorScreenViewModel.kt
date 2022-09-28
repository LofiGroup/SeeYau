package com.lofigroup.features.navigator_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.domain.navigator.usecases.GetNearbyUsersUseCase
import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.model.toUIModel
import com.sillyapps.core_time.Time
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavigatorScreenViewModel @Inject constructor(
  private val getNearbyUsersUseCase: GetNearbyUsersUseCase,

) : ViewModel(), NavigatorScreenStateHolder {

  private val state = MutableStateFlow(NavigatorScreenState())

  init {
    viewModelScope.launch {
      getNearbyUsersUseCase().collect { users ->
        applyUpdates(users)
      }
    }
  }

  override fun getState(): Flow<NavigatorScreenState> {
    return state
  }

  override fun selectUser(id: Long) {
    state.apply {
      val selectedUser = value.sortedUsers.firstOrNull { it.id == id }
      value = value.copy(selectedUser = selectedUser)
    }
  }

  override fun onToggleFullScreenMode() {
    state.apply { value = value.copy(fullScreenMode = !value.fullScreenMode) }
  }

  override fun onIgnoreSelectedUser() {

  }

  override fun onShowChat() {
    state.apply { value = value.copy(chatIsVisible = true) }
  }

  private fun applyUpdates(nearbyUsers: List<NearbyUser>) {
    val sorted = nearbyUsers.sortedBy {
      it.lastConnection
    }.map { it.toUIModel() }

    val splitIndex = sorted.indexOfFirst {
      it.lastConnection > System.currentTimeMillis() - 1 * Time.m
    } + 1

    val selectedUser = sorted.firstOrNull { it.id == state.value.selectedUser?.id }

    state.value = state.value.copy(
      selectedUser = selectedUser,
      sortedUsers = sorted,
      splitIndex = splitIndex
    )
  }
}
