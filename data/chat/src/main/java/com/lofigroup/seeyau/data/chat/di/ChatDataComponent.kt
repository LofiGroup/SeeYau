package com.lofigroup.seeyau.data.chat.di

import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.chat.ChatRepositoryImpl
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.*
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@AppScope
@Component(modules = [IOModule::class, RepositoryModule::class, ApiModule::class, DataDIModule::class])
interface ChatDataComponent {

  fun getRepository(): ChatRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun webSocketChannel(webSocketChannel: WebSocketChannel): Builder

    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun profileDataSource(profileDataSource: ProfileDataSource): Builder

    @BindsInstance
    fun chatDao(chatDao: ChatDao): Builder

    @BindsInstance
    fun userDao(userDao: UserDao): Builder

    @BindsInstance
    fun profileRepository(profileRepository: ProfileRepository): Builder

    @BindsInstance
    fun ioScope(scope: CoroutineScope): Builder

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