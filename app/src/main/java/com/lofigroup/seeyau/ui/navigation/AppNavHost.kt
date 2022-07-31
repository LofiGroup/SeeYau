package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.api.NavigatorScreenNavigation

@Composable
fun AppNavHost(
  navController: NavHostController,
  navigatorComponent: NavigatorComponent,
  modifier: Modifier
) {

  NavHost(
    navController = navController,
    startDestination = Screen.NavigatorScreen.route,
    modifier = modifier
  ) {

    composable(route = Screen.NavigatorScreen.route) {
      NavigatorScreenNavigation(
        navigatorComponent = navigatorComponent
      )
    }

    composable(route = Screen.DialogScreen.route) {

    }

    composable(route = Screen.NotificationScreen.route) {

    }

    composable(route = Screen.SettingsScreen.route) {

    }

  }

}