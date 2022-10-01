package com.lofigroup.features.navigator_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.di.DaggerNavigatorScreenComponent
import com.lofigroup.features.navigator_screen.ui.NavigatorScreen
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun NavigatorScreenNavigation(
  navigatorComponent: NavigatorComponent,
  chatComponent: ChatComponent,
  onNavigateToChat: (Long) -> Unit,
  onNavigateToSettings: () -> Unit,
  onNavigateToChatList: () -> Unit
) {

  val component = DaggerNavigatorScreenComponent.builder()
    .navigatorComponent(navigatorComponent)
    .chatComponent(chatComponent)
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