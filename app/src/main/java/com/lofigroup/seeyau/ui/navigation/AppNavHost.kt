package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lofigroup.features.navigator_screen.api.NavigatorScreenNavigation
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.features.auth_screen_flow.api.AuthScreenFlowNavigation
import com.lofigroup.seeyau.features.chat.api.ChatScreenNavigation
import com.lofigroup.seeyau.features.chat_screen.api.ChatListScreenNavigation
import com.lofigroup.seeyau.features.profile_screen.api.ProfileScreenNavigation
import com.lofigroup.seeyau.features.splash_screen.api.SplashScreenNavigation
import com.sillyapps.core.ui.util.navigateBackTo
import com.sillyapps.core.ui.util.navigateToTopDestination
import timber.log.Timber

@Composable
fun AppNavHost(
  navController: NavHostController,
  appModules: AppModules,

  onStartNearbyService: () -> Unit,
  modifier: Modifier
) {

  NavHost(
    navController = navController,
    startDestination = Screen.SplashScreen.route,
    modifier = modifier
  ) {

    composable(route = Screen.SplashScreen.route) {
      SplashScreenNavigation(
        authComponent = appModules.authModuleImpl.domainComponent(),
        isLoggedIn = { isLoggedIn ->
          if (isLoggedIn) {
            navController.navigateToTopDestination(Screen.PagerScreen.route)
          } else {
            navController.navigateToTopDestination(Screen.AuthScreen.route)
          }
        }
      )
    }

    composable(route = Screen.PagerScreen.route) {
      onStartNearbyService()
      NavigationPager(
        appModules = appModules,
      )
    }

    composable(route = Screen.AuthScreen.route) {
      AuthScreenFlowNavigation(
        authComponent = appModules.authModuleImpl.domainComponent(),
        profileComponent = appModules.profileModule.domainComponent,
        baseComponent = appModules.baseDataModule.domainComponent,
        isDone = {
          navController.navigateToTopDestination(Screen.PagerScreen.route)
        }
      )
    }

  }

}