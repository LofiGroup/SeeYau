package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.features.auth_screen_flow.api.AuthScreenFlowNavigation
import com.lofigroup.seeyau.features.chat.ui.composition_locals.LocalFileDownloader
import com.lofigroup.seeyau.features.splash_screen.api.SplashScreenNavigation
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenOptions
import com.sillyapps.core.ui.util.navigateToTopDestination

@Composable
fun AppNavHost(
  mainScreenEvent: MainScreenEvent?,

  navController: NavHostController,
  appModules: AppModules,

  onStartNearbyService: () -> Unit,
  modifier: Modifier
) {

  var event by remember {
    mutableStateOf(mainScreenEvent)
  }

  CompositionLocalProvider(
    LocalFileDownloader provides appModules.baseDataModule.component.getFileDownloader()
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
          },
          splashScreenOptions = SplashScreenOptions(
            withoutDelay = event is MainScreenEvent.OpenChat
          )
        )
      }

      composable(route = Screen.PagerScreen.route) {
        LaunchedEffect(Unit) {
          onStartNearbyService()
        }

        NavigationPager(
          appModules = appModules,
          mainScreenEvent = event,
          navigateTo = { navController.navigateToTopDestination(it) },
          onBackButtonClick = { navController.navigateUp() }
        )
      }

      composable(route = Screen.AuthScreen.route) {
        event = null
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

}