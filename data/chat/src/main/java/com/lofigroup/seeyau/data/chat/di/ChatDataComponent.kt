package com.lofigroup.seeyau.data.chat.di

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.chat.ChatRepositoryImpl
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.ChatApi
import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.*
import retrofit2.Retrofit

@AppScope
@Component(modules = [IOModule::class, RepositoryModule::class, ApiModule::class])
interface ChatDataComponent {

  fun getRepository(): ChatRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun profileDataSource(profileDataSource: ProfileDataSource): Builder

    @BindsInstance
    fun chatDao(chatDao: ChatDao): Builder

    fun build(): ChatDataComponent
  }

}

@Module
interface RepositoryModule {
  @AppScope
  @Binds
  fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}

@Module
object ApiModule {
  @AppScope
  @Provides
  fun provideChatApi(baseRetrofit: Retrofit): ChatApi = baseRetrofit.create(ChatApi::class.java)
}