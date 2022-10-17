package com.lofigroup.seeyau.data.profile.remote.websocket

import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.backend_api.websocket.WebSocketChannelListener
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.model.toLikeEntity
import com.lofigroup.seeyau.data.profile.remote.websocket.models.ProfileWebsocketResponse
import com.lofigroup.seeyau.data.profile.remote.websocket.models.toLikeEntity
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AppScope
class ProfileWebSocketListener @Inject constructor(
  private val webSocketChannel: WebSocketChannel,
  private val likeDao: LikeDao,
  private val ioScope: CoroutineScope,
  private val ioDispatcher: CoroutineDispatcher,
  private val profileDataSource: ProfileDataSource
): WebSocketChannelListener {
  init {
    webSocketChannel.registerListener("profile", this)
  }

  override fun onMessage(message: String) {
    val response = ProfileWebsocketResponse.adapter.fromJson(message) ?: return

    when (response) {
      is ProfileWebsocketResponse.LikeIsUpdated -> {
        ioScope.launch(ioDispatcher) {
          likeDao.insert(response.toLikeEntity())
        }
      }
    }
  }


}