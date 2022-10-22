package com.lofigroup.seeyau.domain.chat.di

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.usecases.*
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface ChatComponent {

  fun getRepository(): ChatRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun repository(chatRepository: ChatRepository): Builder

    fun build(): ChatComponent
  }

}