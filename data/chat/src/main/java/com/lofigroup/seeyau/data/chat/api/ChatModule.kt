package com.lofigroup.seeyau.data.chat.api

import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.seeyau.data.chat.di.DaggerChatDataComponent
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.domain.chat.di.DaggerChatComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class ChatModule(
  chatDao: ChatDao,
  sharedPreferences: SharedPreferences,
  baseRetrofit: Retrofit,
  webSocketChannel: WebSocketChannel,
  ioScope: CoroutineScope,
  profileDataHandler: ProfileDataHandler
) {

  val dataComponent = DaggerChatDataComponent.builder()
    .chatDao(chatDao)
    .sharedPref(sharedPreferences)
    .baseRetrofit(baseRetrofit)
    .profileDataHandler(profileDataHandler)
    .ioScope(ioScope)
    .webSocketChannel(webSocketChannel)
    .build()

  val domainComponent = DaggerChatComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}