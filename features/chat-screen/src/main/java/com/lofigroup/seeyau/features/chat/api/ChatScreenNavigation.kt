package com.lofigroup.seeyau.features.chat.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.features.chat.ChatScreen
import com.lofigroup.seeyau.features.chat.di.DaggerChatScreenComponent
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ChatScreenNavigation(
  chatComponent: ChatComponent,
  chatId: Long,
  onUpButtonClick: () -> Unit
) {

  val component = DaggerChatScreenComponent.builder()
    .chatComponent(chatComponent)
    .chatId(chatId)
    .resources(LocalContext.current.resources)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  ChatScreen(
    stateHolder = viewModel,
    onUpButtonClick = onUpButtonClick
  )

}