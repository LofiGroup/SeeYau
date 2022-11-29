package com.lofigroup.seeyau.features.chat.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.chat.ChatScreen
import com.lofigroup.seeyau.features.chat.di.DaggerChatScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ChatScreenNavigation(
  chatComponent: ChatComponent,
  profileComponent: ProfileComponent,
  chatId: Long,
  onUpButtonClick: () -> Unit
) {

  val component = DaggerChatScreenComponent.builder()
    .chatComponent(chatComponent)
    .profileComponent(profileComponent)
    .chatId(chatId)
    .resources(LocalContext.current.resources)
    .context(LocalContext.current)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  ChatScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick
  )

}