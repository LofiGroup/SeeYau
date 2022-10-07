package com.lofigroup.seeyau.domain.chat.di

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.usecases.*
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface ChatComponent {

  fun observeChatsUseCase(): ObserveChatsUseCase
  fun observeChatUseCase(): ObserveChatUseCase
  fun pullChatDataUseCase(): PullChatDataUseCase
  fun observeChatEvents(): ObserveChatEventsUseCase
  fun sendChatMessageUseCase(): SendChatMessageUseCase
  fun markChatAsRead(): MarkChatAsReadUseCase
  fun getChatIdByUserIdUseCase(): GetChatIdByUserIdUseCase
  fun getUserIdByChatIdUseCase(): GetUserIdByChatIdUseCase

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun repository(chatRepository: ChatRepository): Builder

    fun build(): ChatComponent
  }

}