package com.lofigroup.data.navigator.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.seeyau.common.profile.notifications.ProfileNotificationBuilder
import com.lofigroup.seeyau.data.chat.ChatDataHandler
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import com.sillyapps.core.ui.app_lifecycle.AppLifecycle
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@AppScope
@Component(
  modules = [ApiModule::class, RepositoryModule::class, IOModule::class]
)
interface NavigatorDataComponent {

  fun getRepository(): NavigatorRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun profileDataHandler(profileDataHandler: ProfileDataHandler): Builder

    @BindsInstance
    fun chatDataHandler(chatDataHandler: ChatDataHandler): Builder

    @BindsInstance
    fun sharedPref(sharedPref: SharedPreferences): Builder

    @BindsInstance
    fun appScope(appScope: CoroutineScope): Builder

    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun websocketChannel(webSocketChannel: WebSocketChannel): Builder

    @BindsInstance
    fun appLifecycle(appLifecycle: AppLifecycle): Builder

    @BindsInstance
    fun profileNotificationBuilder(profileNotificationBuilder: ProfileNotificationBuilder): Builder

    fun build(): NavigatorDataComponent
  }

}