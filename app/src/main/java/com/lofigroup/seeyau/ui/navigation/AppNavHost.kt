package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.api.NavigatorScreenNavigation
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.auth_screen.api.AuthNavigation
import com.lofigroup.seeyau.features.profile_screen.api.ProfileScreenNavigation
import com.lofigroup.seeyau.features.splash_screen.api.SplashScreenNavigation
import com.sillyapps.core.ui.util.navigateToTopDestination

@Composable
fun AppNavHost(
  navController: NavHostController,

  navigatorComponent: NavigatorComponent,
  authComponent: AuthComponent,
  profileComponent: ProfileComponent,

  startNearbyService: () -> Unit,
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
        authComponent = authComponent,
        isLoggedIn = { isLoggedIn ->
          if (isLoggedIn) {
            navController.navigateToTopDestination(Screen.NavigatorScreen.route)
          } else {
            navController.navigateToTopDestination(Screen.AuthScreen.route)
          }
        }
      )
    }

    composable(route = Screen.NavigatorScreen.route) {
      setBottomBarVisibility(true)
      startNearbyService()
      NavigatorScreenNavigation(
        navigatorComponent = navigatorComponent
      )
    }

    composable(route = Screen.AuthScreen.route) {
      setBottomBarVisibility(false)
      AuthNavigation(
        authComponent = authComponent,
        onLoggedIn = { justRegistered ->
          val route =
            if (justRegistered) Screen.ProfileScreen.route else Screen.NavigatorScreen.route
          navController.navigateToTopDestination(route)
        }
      )
    }

    composable(route = Screen.ProfileScreen.route) {
      ProfileScreenNavigation(
        profileComponent = profileComponent,
        onExit = { navController.navigateToTopDestination(Screen.NavigatorScreen.route) }
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