package com.lofigroup.seeyau.data.chat.api

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.chat.di.ChatDataComponent
import com.lofigroup.seeyau.data.chat.di.DaggerChatDataComponent
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.lofigroup.seeyau.domain.chat.di.DaggerChatComponent
import retrofit2.Retrofit

class ChatModule(
  chatDao: ChatDao,
  sharedPreferences: SharedPreferences,
  baseRetrofit: Retrofit,
  profileDataSource: ProfileDataSource
) {

  private val dataComponent = DaggerChatDataComponent.builder()
    .chatDao(chatDao)
    .sharedPref(sharedPreferences)
    .baseRetrofit(baseRetrofit)
    .profileDataSource(profileDataSource)
    .build()

  val domainComponent = DaggerChatComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}