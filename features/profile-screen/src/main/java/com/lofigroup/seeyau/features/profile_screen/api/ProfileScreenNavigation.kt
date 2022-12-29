package com.lofigroup.seeyau.features.profile_screen.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.lofigroup.seeyau.features.profile_screen.ProfileScreen
import com.lofigroup.seeyau.features.profile_screen.di.DaggerProfileScreenComponent
import com.lofigroup.seeyau.features.profile_screen.di.ProfileScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ProfileScreenNavigation(
  component: ProfileScreenComponent,
  onUpButtonClick: () -> Unit,
  onNavigateToAuthScreen: () -> Unit,
  isFocused: Boolean
) {

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

@Composable
fun rememberProfileScreenComponent(
  profileComponent: ProfileComponent,
  settingsComponent: SettingsComponent,
  authComponent: AuthComponent,
): ProfileScreenComponent {
  return remember {
    DaggerProfileScreenComponent.builder()
      .profileComponent(profileComponent)
      .settingsComponent(settingsComponent)
      .authComponent(authComponent)
      .build()
  }
}