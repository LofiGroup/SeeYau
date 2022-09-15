package com.lofigroup.seeyau.domain.chat.di

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.usecases.GetChatUseCase
import com.lofigroup.seeyau.domain.chat.usecases.GetChatsUseCase
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.domain.chat.usecases.SendChatMessageUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface ChatComponent {

  fun getChatsUseCase(): GetChatsUseCase
  fun pullChatDataUseCase(): PullChatDataUseCase
  fun getChatUseCase(): GetChatUseCase
  fun sendChatMessageUseCase(): SendChatMessageUseCase

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun repository(chatRepository: ChatRepository): Builder

    fun build(): ChatComponent
  }

}