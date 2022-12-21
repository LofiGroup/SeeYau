package com.lofigroup.seeyau.features.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.auth.model.LoggedInStatus
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import com.lofigroup.seeyau.features.splash_screen.model.State
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
  private val isLoggedInUseCase: IsLoggedInUseCase
): ViewModel(), SplashScreenStateHolder {

  private val state = MutableStateFlow(SplashScreenState())

  init {
    load()
  }

  override fun getState(): Flow<SplashScreenState> = state

  override fun load() {
    state.value = state.value.copy(state = State.Loading)

    viewModelScope.launch {
      val delayDeff = async { delay(2500L) }
      val checkTokenDeff = async { checkToken() }

      delayDeff.await()
      val result = checkTokenDeff.await()
      state.value = state.value.copy(state = result)
    }
  }

  private suspend fun checkToken(): State {
    val result = isLoggedInUseCase()

    return when (result) {
      LoggedInStatus.CantAccessServer -> State.IsLoggedIn
      LoggedInStatus.InvalidToken -> State.ShouldAuthorize
      LoggedInStatus.LoggedIn -> State.IsLoggedIn
      is LoggedInStatus.UnknownError -> State.UnknownError(result.errorMessage)
    }
  }
}