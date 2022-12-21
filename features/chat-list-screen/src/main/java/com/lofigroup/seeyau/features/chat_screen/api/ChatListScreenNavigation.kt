package com.lofigroup.seeyau.features.chat_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.lofigroup.seeyau.features.chat_screen.di.DaggerChatListScreenComponent
import com.lofigroup.seeyau.features.chat_screen.ui.ChatListScreen
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ChatListScreenNavigation(
  chatComponent: ChatComponent,
  profileComponent: ProfileComponent,
  settingsComponent: SettingsComponent,
  onNavigateToChatScreen: (Long) -> Unit,
  onNavigateToSettingsScreen: () -> Unit
) {

  val component = DaggerChatListScreenComponent.builder()
    .chatComponent(chatComponent)
    .profileComponent(profileComponent)
    .settingsComponent(settingsComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  ChatListScreen(
    stateHolder = viewModel,
    onNavigateToChatScreen = onNavigateToChatScreen,
    onNavigateToSettingsScreen = onNavigateToSettingsScreen
  )

}