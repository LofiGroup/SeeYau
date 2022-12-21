package com.lofigroup.seeyau.features.chat.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.chat.ui.ChatScreen
import com.lofigroup.seeyau.features.chat.di.DaggerChatScreenComponent
import com.sillyapps.core.ui.daggerViewModel
import timber.log.Timber

@Composable
fun ChatScreenNavigation(
  chatComponent: ChatComponent,
  profileComponent: ProfileComponent,
  permissionChannel: PermissionRequestChannel,
  chatId: Long,
  onUpButtonClick: () -> Unit,
  isFocused: Boolean
) {

  val context = LocalContext.current
  val component = remember {
    DaggerChatScreenComponent.builder()
      .chatComponent(chatComponent)
      .profileComponent(profileComponent)
      .permissionChannel(permissionChannel)
      .resources(context.resources)
      .context(context)
      .build()
  }

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  LaunchedEffect(key1 = chatId) {
    Timber.e("New Chat id is $chatId")
    viewModel.setChatId(chatId)
  }

  ChatScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick,
    isFocused = isFocused
  )

}