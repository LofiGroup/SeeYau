package com.lofigroup.seeyau.data.profile.api

import android.content.ContentResolver
import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.di.DaggerProfileDataComponent
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.domain.profile.di.DaggerProfileComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class ProfileModule(
  appScope: CoroutineScope,
  baseRetrofit: Retrofit,
  webSocketChannel: WebSocketChannel,
  userDao: UserDao,
  likeDao: LikeDao,
  sharedPref: SharedPreferences,
  contentResolver: ContentResolver
) {

  val dataComponent = DaggerProfileDataComponent.builder()
    .appScope(appScope)
    .baseRetrofit(baseRetrofit)
    .webSocketChannel(webSocketChannel)
    .userDao(userDao)
    .likeDao(likeDao)
    .sharedPref(sharedPref)
    .contentResolver(contentResolver)
    .build()

  val domainComponent = DaggerProfileComponent.builder()
    .profileRepository(dataComponent.getRepository())
    .build()

}