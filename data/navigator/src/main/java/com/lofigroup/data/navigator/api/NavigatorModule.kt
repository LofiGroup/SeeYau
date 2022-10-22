package com.lofigroup.data.navigator.api

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.data.navigator.di.DaggerNavigatorDataComponent
import com.lofigroup.domain.navigator.di.DaggerNavigatorComponent
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class NavigatorModule(
  context: Context,
  userDao: UserDao,
  chatDao: ChatDao,
  likeDao: LikeDao,
  blacklistDao: BlacklistDao,
  profileRepository: ProfileRepository,
  sharedPreferences: SharedPreferences,
  appScope: CoroutineScope,
  baseRetrofit: Retrofit,
  webSocketChannel: WebSocketChannel
) {

  private val dataComponent = DaggerNavigatorDataComponent.builder()
    .appScope(appScope)
    .baseRetrofit(baseRetrofit)
    .websocketChannel(webSocketChannel)
    .sharedPref(sharedPreferences)
    .userDao(userDao)
    .chatDao(chatDao)
    .likeDao(likeDao)
    .context(context)
    .blacklistDao(blacklistDao)
    .build()

  val domainComponent = DaggerNavigatorComponent.builder()
    .repository(dataComponent.getRepository())
    .profileRepository(profileRepository)
    .build()

}