package com.lofigroup.seeyau.features.auth_screen_flow.api

import androidx.compose.runtime.Composable
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.auth_screen_flow.di.DaggerAuthScreenFlowComponent
import com.lofigroup.seeyau.features.auth_screen_flow.ui.AuthScreenRoot
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun AuthScreenFlowNavigation(
  authComponent: AuthComponent,
  profileComponent: ProfileComponent,
  isDone: () -> Unit
) {

  val component = DaggerAuthScreenFlowComponent.builder()
    .authComponent(authComponent)
    .profileComponent(profileComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  AuthScreenRoot(
    stateHolder = viewModel,
    isDone = isDone
  )

}