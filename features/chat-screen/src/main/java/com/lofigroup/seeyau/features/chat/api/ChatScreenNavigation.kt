package com.lofigroup.seeyau.features.chat.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.chat.ui.ChatScreen
import com.lofigroup.seeyau.features.chat.di.DaggerChatScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ChatScreenNavigation(
  chatComponent: ChatComponent,
  profileComponent: ProfileComponent,
  permissionChannel: PermissionRequestChannel,
  chatId: Long,
  onUpButtonClick: () -> Unit,
  isFocused: Boolean
) {

  val component = DaggerChatScreenComponent.builder()
    .chatComponent(chatComponent)
    .profileComponent(profileComponent)
    .permissionChannel(permissionChannel)
    .chatId(chatId)
    .resources(LocalContext.current.resources)
    .context(LocalContext.current)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  ChatScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick,
    isFocused = isFocused
  )

}