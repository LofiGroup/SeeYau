package com.lofigroup.seeyau.features.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
  private val isLoggedInUseCase: IsLoggedInUseCase
): ViewModel(), SplashScreenStateHolder {

  private val state = MutableStateFlow(SplashScreenState())

  init {
    viewModelScope.launch {
      state.value = when (val result = isLoggedInUseCase()) {
        is Result.Error -> state.value.copy(isLoggedIn = false, isReady = true)
        Result.Success -> state.value.copy(isLoggedIn = true, isReady = true)
        is Result.Undefined -> {
          state.value.copy(isLoggedIn = false, isReady = false, errorMessage = result.message)
        }
      }
    }
  }

  override fun getState(): Flow<SplashScreenState> = state
}