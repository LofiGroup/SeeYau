package com.lofigroup.seeyau.data.chat.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.seeyau.common.chat.components.notifications.ChatNotificationBuilder
import com.lofigroup.seeyau.data.chat.ChatDataHandler
import com.lofigroup.seeyau.data.chat.ChatRepositoryImpl
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.domain.base.user_notification_channel.UserNotificationChannel
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.*
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@AppScope
@Component(modules = [IOModule::class, RepositoryModule::class, ApiModule::class, DataDIModule::class])
interface ChatDataComponent {

  fun getRepository(): ChatRepository
  fun chatDataHandler(): ChatDataHandler

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun webSocketChannel(webSocketChannel: WebSocketChannel): Builder

    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun chatDao(chatDao: ChatDao): Builder

    @BindsInstance
    fun profileDataHandler(profileDataHandler: ProfileDataHandler): Builder

    @BindsInstance
    fun ioScope(scope: CoroutineScope): Builder

    @BindsInstance
    fun userNotificationChannel(userNotificationChannel: UserNotificationChannel): Builder

    @BindsInstance
    fun chatNotificationBuilder(chatNotificationBuilder: ChatNotificationBuilder): Builder

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