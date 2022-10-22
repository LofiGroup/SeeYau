package com.lofigroup.seeyau.data.chat.api

import android.content.SharedPreferences
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.core.util.EventChannel
import com.lofigroup.seeyau.data.chat.di.DaggerChatDataComponent
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.events.ProfileChannelEvent
import com.lofigroup.seeyau.domain.chat.di.DaggerChatComponent
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class ChatModule(
  chatDao: ChatDao,
  userDao: UserDao,
  likeDao: LikeDao,
  sharedPreferences: SharedPreferences,
  baseRetrofit: Retrofit,
  webSocketChannel: WebSocketChannel,
  profileDataSource: ProfileDataSource,
  ioScope: CoroutineScope,
  profileRepository: ProfileRepository,
  profileEventChannel: EventChannel<ProfileChannelEvent>
) {

  private val dataComponent = DaggerChatDataComponent.builder()
    .chatDao(chatDao)
    .userDao(userDao)
    .likeDao(likeDao)
    .sharedPref(sharedPreferences)
    .baseRetrofit(baseRetrofit)
    .profileDataSource(profileDataSource)
    .ioScope(ioScope)
    .profileRepository(profileRepository)
    .webSocketChannel(webSocketChannel)
    .profileEventChannel(profileEventChannel)
    .build()

  val domainComponent = DaggerChatComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}