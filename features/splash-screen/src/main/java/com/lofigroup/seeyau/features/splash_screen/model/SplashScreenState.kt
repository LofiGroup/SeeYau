package com.lofigroup.seeyau.features.splash_screen.model

data class SplashScreenState(
  val state: State = State.Loading
)

sealed interface State {
  object Loading: State
  object IsLoggedIn: State
  object ShouldAuthorize: State
  class UnknownError(val message: String): State
}
