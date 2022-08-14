package com.lofigroup.seeyau.features.auth_screen.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.features.auth_screen.AuthScreen
import com.lofigroup.seeyau.features.auth_screen.di.DaggerAuthScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun AuthNavigation(
  authComponent: AuthComponent,
  onLoggedIn: (Boolean) -> Unit
) {
  val component = DaggerAuthScreenComponent.builder()
    .authComponent(authComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getAuthScreenViewModel()
  }

  AuthScreen(
    stateHolder = viewModel,
    onSuccessfulSign = onLoggedIn
  )
}