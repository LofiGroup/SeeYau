package com.lofigroup.seeyau.domain.chat.di

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.usecases.GetChatsUseCase
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import dagger.BindsInstance
import dagger.Component

@Component()
interface ChatComponent {

  fun getChatsUseCase(): GetChatsUseCase
  fun pullChatDataUseCase(): PullChatDataUseCase

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun repository(chatRepository: ChatRepository): Builder

    fun build(): ChatComponent
  }

}