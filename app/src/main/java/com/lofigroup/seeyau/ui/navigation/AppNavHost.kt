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
import com.sillyapps.core.ui.util.navigateToTopDestination
import timber.log.Timber

@Composable
fun AppNavHost(
  navController: NavHostController,
  appModules: AppModules,

  onAuthorized: () -> Unit,
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
            navController.navigateToTopDestination(Screen.NavigatorScreen.route)
            onAuthorized()
          } else {
            navController.navigateToTopDestination(Screen.AuthScreen.route)
          }
        }
      )
    }

    composable(route = Screen.NavigatorScreen.route) {
      onStartNearbyService()
      NavigatorScreenNavigation(
        navigatorComponent = appModules.navigatorModule.domainComponent,
        onNavigateToChatList = { navController.navigate(Screen.ChatListScreen.route) },
        onNavigateToSettings = { navController.navigate(Screen.SettingsScreen.route) },
        onNavigateToChat = { navController.navigate("${Screen.ChatScreen.route}/$it") }
      )
    }

    composable(route = Screen.AuthScreen.route) {
      AuthScreenFlowNavigation(
        authComponent = appModules.authModuleImpl.domainComponent(),
        profileComponent = appModules.profileModule.domainComponent,
        isDone = {
          onAuthorized()
          navController.navigateToTopDestination(Screen.NavigatorScreen.route)
        }
      )
    }

    composable(route = Screen.SettingsScreen.route) {
      ProfileScreenNavigation(
        profileComponent = appModules.profileModule.domainComponent,
        settingsComponent = appModules.settingsModule.domainComponent,
        onUpButtonClick = { navController.navigateToTopDestination(Screen.NavigatorScreen.route) }
      )
    }

    composable(
      route = "${Screen.ChatScreen.route}/{chatId}",
      arguments = listOf(navArgument(name = "chatId") {
        type = NavType.LongType
      })
    ) {
      val chatId = it.arguments?.getLong("chatId")

      if (chatId == null) {
        LaunchedEffect(chatId) {
          Timber.e("Malformed argument: chatId")
          navController.popBackStack()
        }
      }
      else {
        ChatScreenNavigation(
          chatComponent = appModules.chatModule.domainComponent,
          chatId = chatId,
          onUpButtonClick = { navController.popBackStack() }
        )
      }

    }

    composable(route = Screen.ChatListScreen.route) {
      ChatListScreenNavigation(
        chatComponent = appModules.chatModule.domainComponent,
        onItemClick = { navController.navigate(
          "${Screen.ChatScreen.route}/$it"
        )
        },
        onUpButtonClick = {
          navController.popBackStack()
        }
      )
    }

  }

}