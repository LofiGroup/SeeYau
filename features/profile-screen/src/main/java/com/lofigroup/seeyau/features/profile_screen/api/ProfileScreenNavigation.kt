package com.lofigroup.seeyau.features.profile_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.profile_screen.ProfileScreen
import com.lofigroup.seeyau.features.profile_screen.di.DaggerProfileScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ProfileScreenNavigation(
  profileComponent: ProfileComponent,
  onUpButtonClick: () -> Unit
) {
  
  val component = DaggerProfileScreenComponent.builder()
    .profileComponent(profileComponent)
    .build()
  
  val viewModel = daggerViewModel {
    component.getViewModel()
  }
  
  ProfileScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick
  )
  
}