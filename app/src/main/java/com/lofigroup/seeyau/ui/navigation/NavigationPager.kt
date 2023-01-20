package com.lofigroup.seeyau.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.features.chat.api.ChatScreenNavigation
import com.lofigroup.seeyau.features.chat.api.rememberChatScreenComponent
import com.lofigroup.seeyau.features.chat_screen.api.ChatListScreenNavigation
import com.lofigroup.seeyau.features.chat_screen.api.rememberChatListScreenComponent
import com.lofigroup.seeyau.features.profile_screen.api.ProfileScreenNavigation
import com.lofigroup.seeyau.features.profile_screen.api.rememberProfileScreenComponent
import com.lofigroup.seeyau.ui.permission.NotificationPermission
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationPager(
  appModules: AppModules,
  mainScreenEvent: MainScreenEvent?,
  navigateTo: (String) -> Unit,
  onBackButtonClick: () -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = when (mainScreenEvent) {
    is MainScreenEvent.OpenChat -> 2
    else -> 1
  })

  var currentChatId by rememberSaveable {
    mutableStateOf(if (mainScreenEvent is MainScreenEvent.OpenChat) mainScreenEvent.chatId else 0L)
  }

  val profileScreenComponent = rememberProfileScreenComponent(
    profileComponent = appModules.profileModule.domainComponent,
    settingsComponent = appModules.settingsModule.domainComponent,
    authComponent = appModules.authModuleImpl.domainComponent()
  )

  val chatListScreenComponent = rememberChatListScreenComponent(
    chatComponent = appModules.chatModule.domainComponent,
    profileComponent = appModules.profileModule.domainComponent,
    settingsComponent = appModules.settingsModule.domainComponent
  )

  val chatScreenComponent = rememberChatScreenComponent(
    chatComponent = appModules.chatModule.domainComponent,
    profileComponent = appModules.profileModule.domainComponent,
    permissionChannel = appModules.appComponent.getPermissionChannel()
  )

  LaunchedEffect(Unit) {
    val permissionChannel = appModules.appComponent.getPermissionChannel()
    permissionChannel.requestPermission(NotificationPermission)
  }

  HorizontalPager(
    state = pagerState,
    key = { it },
    count = if (currentChatId != 0L) 3 else 2
  ) { page ->
    when (page) {
      PROFILE_SCREEN -> {
        ProfileScreenNavigation(
          component = profileScreenComponent,
          onUpButtonClick = {
            coroutineScope.launch { pagerState.animateScrollToPage(1) }
          },
          onNavigateToAuthScreen = { navigateTo(Screen.AuthScreen.route) },
          isFocused = currentPage == page
        )
      }
      MAIN_SCREEN -> {
        ChatListScreenNavigation(
          component = chatListScreenComponent,
          onNavigateToChatScreen = {
            currentChatId = it
            coroutineScope.launch { pagerState.animateScrollToPage(CHAT_SCREEN) }
          },
          onNavigateToSettingsScreen = {
            coroutineScope.launch { pagerState.animateScrollToPage(PROFILE_SCREEN) }
          }
        )
      }
      CHAT_SCREEN -> {
        ChatScreenNavigation(
          component = chatScreenComponent,
          chatId = currentChatId,
          onUpButtonClick = {
            coroutineScope.launch { pagerState.animateScrollToPage(MAIN_SCREEN) }
          },
          isFocused = currentPage == page
        )
      }
    }
  }
}

const val PROFILE_SCREEN = 0
const val MAIN_SCREEN = 1
const val CHAT_SCREEN = 2