package com.lofigroup.seeyau.features.splash_screen

import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.splash_screen.components.LoadingScreen
import com.lofigroup.seeyau.features.splash_screen.components.UnknownErrorScreen
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import com.lofigroup.seeyau.features.splash_screen.model.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
  stateHolder: SplashScreenStateHolder,
  isLoggedIn: (Boolean) -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = SplashScreenState())

  Surface {
    when (val screenState = state.state) {
      State.IsLoggedIn -> {
        LaunchedEffect(state) {
          isLoggedIn(true)
        }
      }
      State.Loading -> LoadingScreen()
      State.ShouldAuthorize -> {
        LaunchedEffect(state) {
          isLoggedIn(true)
        }
      }
      is State.UnknownError -> UnknownErrorScreen(
        message = screenState.message,
        onTryAgain = stateHolder::load
      )
    }
  }

}

@Preview
@Composable
fun PreviewSplashScreen() {
  val scope = rememberCoroutineScope()

  val stateHolder = object : SplashScreenStateHolder {
    private val state = MutableStateFlow(SplashScreenState())

    init {
      scope.launch {
        delay(2500L)
        state.value = state.value.copy()
      }
    }

    override fun getState(): Flow<SplashScreenState> {
      return state
    }

    override fun load() {

    }
  }

  AppTheme {
    SplashScreen(
      stateHolder = stateHolder,
      isLoggedIn = {}
    )
  }
}