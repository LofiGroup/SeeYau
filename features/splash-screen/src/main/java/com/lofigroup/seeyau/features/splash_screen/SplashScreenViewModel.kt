package com.lofigroup.seeyau.features.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
  private val getTokenUseCase: IsLoggedInUseCase
): ViewModel(), SplashScreenStateHolder {

  private val state = MutableStateFlow(SplashScreenState())

  init {
    viewModelScope.launch {
      val isLoggedIn = getTokenUseCase()
      state.value = state.value.copy(
        isLoggedIn = isLoggedIn,
        isReady = true
      )
    }
  }

  override fun getState(): Flow<SplashScreenState> = state
}