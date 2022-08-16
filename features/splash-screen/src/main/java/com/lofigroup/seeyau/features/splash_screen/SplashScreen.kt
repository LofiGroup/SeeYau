package com.lofigroup.seeyau.features.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import com.sillyapps.core.ui.components.ShowToast

@Composable
fun SplashScreen(
  stateHolder: SplashScreenStateHolder,
  isLoggedIn: (Boolean) -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = SplashScreenState())

  if (state.isReady) {
    LaunchedEffect(state) {
      isLoggedIn(state.isLoggedIn)
    }
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    CircularProgressIndicator(
      modifier = Modifier.align(Alignment.Center)
    )
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }

}