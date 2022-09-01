package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.api.NavigatorScreenNavigation
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.auth_screen.api.AuthNavigation
import com.lofigroup.seeyau.features.chat_screen.api.ChatListScreenNavigation
import com.lofigroup.seeyau.features.chat_screen.ui.ChatListScreen
import com.lofigroup.seeyau.features.profile_screen.api.ProfileScreenNavigation
import com.lofigroup.seeyau.features.splash_screen.api.SplashScreenNavigation
import com.sillyapps.core.ui.util.navigateToTopDestination

@Composable
fun AppNavHost(
  navController: NavHostController,
  appModules: AppModules,

  onStart: () -> Unit,
  setBottomBarVisibility: (Boolean) -> Unit,
  modifier: Modifier
) {

  NavHost(
    navController = navController,
    startDestination = Screen.SplashScreen.route,
    modifier = modifier
  ) {

    composable(route = Screen.SplashScreen.route) {
      SplashScreenNavigation(
        authComponent = appModules.authModule.domainComponent,
        isLoggedIn = { isLoggedIn ->
          if (isLoggedIn) {
            navController.navigateToTopDestination(Screen.NavigatorScreen.route)
            onStart()
          } else {
            navController.navigateToTopDestination(Screen.AuthScreen.route)
          }
        }
      )
    }

    composable(route = Screen.NavigatorScreen.route) {
      setBottomBarVisibility(true)
      NavigatorScreenNavigation(
        navigatorComponent = appModules.navigatorModule.domainComponent
      )
    }

    composable(route = Screen.AuthScreen.route) {
      setBottomBarVisibility(false)
      AuthNavigation(
        authComponent = appModules.authModule.domainComponent,
        onLoggedIn = { justRegistered ->
          val route =
            if (justRegistered) Screen.ProfileScreen.route else Screen.NavigatorScreen.route
          navController.navigateToTopDestination(route)
          onStart()
        }
      )
    }

    composable(route = Screen.ProfileScreen.route) {
      ProfileScreenNavigation(
        profileComponent = appModules.profileModule.domainComponent,
        onExit = { navController.navigateToTopDestination(Screen.NavigatorScreen.route) }
      )
    }

    composable(route = Screen.ChatScreen.route) {

    }

    composable(route = Screen.ChatListScreen.route) {
      setBottomBarVisibility(true)
      ChatListScreenNavigation(
        chatComponent = appModules.chatModule.domainComponent,
        onItemClick = {  }
      )
    }

    composable(route = Screen.NotificationScreen.route) {

    }

    composable(route = Screen.SettingsScreen.route) {

    }

  }

}