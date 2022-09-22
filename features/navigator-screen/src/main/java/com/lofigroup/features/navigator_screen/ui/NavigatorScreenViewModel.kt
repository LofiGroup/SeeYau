package com.lofigroup.features.navigator_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.domain.navigator.model.User
import com.lofigroup.domain.navigator.usecases.GetNearbyUsersUseCase
import com.lofigroup.domain.navigator.usecases.GetUserUseCase
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
  private val getUserUseCase: GetUserUseCase
) : ViewModel(), NavigatorScreenStateHolder {

  private val state = MutableStateFlow(NavigatorScreenState())
  private var observeUserJob: Job? = null

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
    observeUserJob?.cancel()

    observeUserJob = viewModelScope.launch {
      getUserUseCase(id).collect {
        applySelectedUserUpdate(it)
      }
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

  private fun applySelectedUserUpdate(user: User) {
    state.value = state.value.copy(
      selectedUser = user.toUIModel()
    )
  }

  private fun applyUpdates(users: List<User>) {
    val sorted = users.sortedBy {
      it.lastConnection
    }
    var splitIndex = sorted.indexOfFirst {
      it.lastConnection > System.currentTimeMillis() - 1 * Time.m
    }
    if (splitIndex == -1) {
      splitIndex = 0
    }
    state.value = state.value.copy(
      nearbyUsers = sorted.subList(0, splitIndex).map { it.toUIModel() },
      metUsers = sorted.subList(splitIndex, sorted.size).map { it.toUIModel() }
    )
  }
}
