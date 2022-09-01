package com.lofigroup.seeyau.features.chat_screen.di

import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.features.chat_screen.ui.ChatListScreenViewModel
import dagger.Component

@Component(dependencies = [ChatComponent::class])
interface ChatListScreenComponent {

  fun getViewModel(): ChatListScreenViewModel

  @Component.Builder
  interface Builder {
    fun chatComponent(chatComponent: ChatComponent): Builder

    fun build(): ChatListScreenComponent
  }

}