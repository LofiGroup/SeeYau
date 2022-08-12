package com.lofigroup.seeyau.features.splash_screen.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.features.splash_screen.BleNotSupportedScreen
import com.lofigroup.seeyau.features.splash_screen.SplashScreen
import com.lofigroup.seeyau.features.splash_screen.di.DaggerSplashScreenComponent
import com.sillyapps.core.ui.daggerViewModel
import com.sillyapps.core.ui.util.isBleSupported

@Composable
fun SplashScreenNavigation(
  authComponent: AuthComponent,
  isLoggedIn: (Boolean) -> Unit
) {

  val component = DaggerSplashScreenComponent.builder()
    .authComponent(authComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  val isBleSupported = isBleSupported(LocalContext.current)

  if (!isBleSupported) {
    BleNotSupportedScreen()
  } else {
    SplashScreen(
      stateHolder = viewModel,
      isLoggedIn = isLoggedIn
    )
  }

}