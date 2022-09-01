package com.lofigroup.seeyau.features.chat_screen.api

import androidx.compose.runtime.Composable
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.features.chat_screen.di.DaggerChatListScreenComponent
import com.lofigroup.seeyau.features.chat_screen.ui.ChatListScreen
import com.sillyapps.core.ui.daggerViewModel

@Composable
fun ChatListScreenNavigation(
  chatComponent: ChatComponent,
  onItemClick: (Long) -> Unit
) {

  val component = DaggerChatListScreenComponent.builder()
    .chatComponent(chatComponent)
    .build()

  val viewModel = daggerViewModel {
    component.getViewModel()
  }

  ChatListScreen(
    stateHolder = viewModel,
    onItemClick = onItemClick
  )

}