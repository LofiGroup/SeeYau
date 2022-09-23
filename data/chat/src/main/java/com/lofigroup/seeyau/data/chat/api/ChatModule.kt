package com.lofigroup.seeyau.data.chat.api

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.chat.di.DaggerChatDataComponent
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.domain.chat.di.DaggerChatComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class ChatModule(
  chatDao: ChatDao,
  userDao: UserDao,
  sharedPreferences: SharedPreferences,
  baseRetrofit: Retrofit,
  profileDataSource: ProfileDataSource,
  httpclient: OkHttpClient,
  ioScope: CoroutineScope
) {

  private val dataComponent = DaggerChatDataComponent.builder()
    .chatDao(chatDao)
    .userDao(userDao)
    .sharedPref(sharedPreferences)
    .baseRetrofit(baseRetrofit)
    .httpClient(httpclient)
    .profileDataSource(profileDataSource)
    .ioScope(ioScope)
    .build()

  val domainComponent = DaggerChatComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}