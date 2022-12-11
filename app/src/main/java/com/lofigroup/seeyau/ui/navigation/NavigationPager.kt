package com.lofigroup.seeyau.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.features.chat.api.ChatScreenNavigation
import com.lofigroup.seeyau.features.chat_screen.api.ChatListScreenNavigation
import com.lofigroup.seeyau.features.profile_screen.api.ProfileScreenNavigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationPager(
  appModules: AppModules,
  navigateTo: (String) -> Unit,
  onBackButtonClick: () -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = 1)
  var currentChatId by rememberSaveable {
    mutableStateOf(0L)
  }

  HorizontalPager(
    state = pagerState,
    count = if (currentChatId != 0L) 3 else 2
  ) { page ->
    when (page) {
      PROFILE_SCREEN -> {
        ProfileScreenNavigation(
          profileComponent = appModules.profileModule.domainComponent,
          settingsComponent = appModules.settingsModule.domainComponent,
          authComponent = appModules.authModuleImpl.domainComponent(),
          onUpButtonClick = {
            coroutineScope.launch { pagerState.animateScrollToPage(1) }
          },
          onNavigateToAuthScreen = { navigateTo(Screen.AuthScreen.route) },
          isFocused = currentPage == page
        )
      }
      MAIN_SCREEN -> {
        ChatListScreenNavigation(
          chatComponent = appModules.chatModule.domainComponent,
          profileComponent = appModules.profileModule.domainComponent,
          onNavigateToChatScreen = {
            currentChatId = it
            coroutineScope.launch { pagerState.animateScrollToPage(2) }
          },
          onNavigateToSettingsScreen = {
            coroutineScope.launch { pagerState.animateScrollToPage(0) }
          }
        )
      }
      CHAT_SCREEN -> {
        if (currentChatId != 0L) {
          ChatScreenNavigation(
            chatComponent = appModules.chatModule.domainComponent,
            profileComponent = appModules.profileModule.domainComponent,
            chatId = currentChatId,
            onUpButtonClick = {
              coroutineScope.launch { pagerState.animateScrollToPage(1) }
            },
            isFocused = currentPage == page
          )
        }
      }
    }
  }
}

const val PROFILE_SCREEN = 0
const val MAIN_SCREEN = 1
const val CHAT_SCREEN = 2