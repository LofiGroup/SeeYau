package com.lofigroup.seeyau.features.chat.di

import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.features.chat.ChatScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.BindsInstance
import dagger.Component

@ScreenScope
@Component(
  dependencies = [ChatComponent::class]
)
interface ChatScreenComponent {
  fun getViewModel(): ChatScreenViewModel

  @Component.Builder
  interface Builder {
    fun chatComponent(chatComponent: ChatComponent): Builder

    @BindsInstance
    fun chatId(id: Long): Builder

    fun build(): ChatScreenComponent
  }
}