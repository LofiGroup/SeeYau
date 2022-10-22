package com.lofigroup.seeyau.data.profile.remote.websocket

import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.backend_api.websocket.WebSocketChannelListener
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.remote.websocket.models.*
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AppScope
class ProfileWebSocketListener @Inject constructor(
  private val webSocketChannel: WebSocketChannel,
  private val likeDao: LikeDao,
  private val blacklistDao: BlacklistDao,
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
      is LikeIsUpdated -> {
        ioScope.launch(ioDispatcher) {
          likeDao.insert(response.toLikeEntity())
        }
      }
      is YouAreBlacklisted -> {
        ioScope.launch {
          blacklistDao.insert(response.toBlacklistEntity())
        }
      }
    }
  }


}