package com.lofigroup.features.navigator_screen.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.di.DaggerNavigatorScreenComponent
import com.lofigroup.features.navigator_screen.ui.NavigatorScreen
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun NavigatorScreenNavigation(
  navigatorComponent: NavigatorComponent,
  chatComponent: ChatComponent,
  profileComponent: ProfileComponent,
  settingsComponent: SettingsComponent,
  onNavigateToChat: (Long) -> Unit,
  onNavigateToSettings: () -> Unit,
  onNavigateToChatList: () -> Unit
) {

  val component = DaggerNavigatorScreenComponent.builder()
    .navigatorComponent(navigatorComponent)
    .chatComponent(chatComponent)
    .profileComponent(profileComponent)
    .settingsComponent(settingsComponent)
    .resources(LocalContext.current.resources)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  NavigatorScreen(
    stateHolder = viewModel,
    onNavigateToChat = onNavigateToChat,
    onNavigateToSettings = onNavigateToSettings,
    onNavigateToChatList = onNavigateToChatList
  )

}