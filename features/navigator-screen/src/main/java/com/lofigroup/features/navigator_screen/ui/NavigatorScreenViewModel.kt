package com.lofigroup.features.navigator_screen.ui

import androidx.lifecycle.ViewModel
import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.usecases.GetNearbyUsersUseCase
import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.model.toUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NavigatorScreenViewModel @Inject constructor(
  private val getNearbyUsersUseCase: GetNearbyUsersUseCase
) : ViewModel(), NavigatorScreenStateHolder {

  override fun getState(): Flow<NavigatorScreenState> {
    return getNearbyUsersUseCase().map {
      when (it) {
        is Resource.Loading -> {
          NavigatorScreenState(isLoading = true)
        }
        is Resource.Error -> {
          NavigatorScreenState(errorMessage = it.errorMessage)
        }
        is Resource.Success -> {
          NavigatorScreenState(users = it.data.map { user -> user.toUIModel() })
        }
      }
    }
  }
}