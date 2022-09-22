package com.lofigroup.features.navigator_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.di.DaggerNavigatorScreenComponent
import com.lofigroup.features.navigator_screen.ui.NavigatorScreen
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun NavigatorScreenNavigation(
  navigatorComponent: NavigatorComponent
) {

  val component = DaggerNavigatorScreenComponent.builder()
    .navigatorComponent(navigatorComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  NavigatorScreen(
    stateHolder = viewModel,
    onNavigateToChat = {},
    onNavigateToSettings = {},
    onNavigateToChatList = {}
  )

}