package com.lofigroup.seeyau.features.profile_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.lofigroup.seeyau.features.profile_screen.ProfileScreen
import com.lofigroup.seeyau.features.profile_screen.di.DaggerProfileScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ProfileScreenNavigation(
  profileComponent: ProfileComponent,
  settingsComponent: SettingsComponent,
  authComponent: AuthComponent,
  onUpButtonClick: () -> Unit,
  onNavigateToAuthScreen: () -> Unit,
  isFocused: Boolean
) {
  
  val component = DaggerProfileScreenComponent.builder()
    .profileComponent(profileComponent)
    .settingsComponent(settingsComponent)
    .authComponent(authComponent)
    .build()
  
  val viewModel = daggerViewModel {
    component.getViewModel()
  }
  
  ProfileScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick,
    onNavigateToAuthScreen = onNavigateToAuthScreen,
    isFocused = isFocused
  )
  
}