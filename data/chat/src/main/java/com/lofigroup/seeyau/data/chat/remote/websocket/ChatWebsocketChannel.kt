package com.lofigroup.seeyau.data.chat.remote.websocket

import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.models.WebSocketRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.WebSocketResponse
import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ChatWebsocketChannel @Inject constructor(
  private val client: OkHttpClient,
  private val chatDao: ChatDao,
  private val ioScope: CoroutineScope,
  private val ioDispatcher: CoroutineDispatcher,
  private val profileDataSource: ProfileDataSource
) : WebSocketListener() {

  private val wsChatRequest =
    Request.Builder().url("wss://${SeeYauApiConstants.baseUrl}/ws/chat/")
      .header("origin", "wss://${SeeYauApiConstants.baseUrl}")
      .build()

  private var webSocket: WebSocket? = null

  fun connect() {
    if (webSocket == null)
      webSocket = client.newWebSocket(wsChatRequest, this)
  }

  fun disconnect() {
    webSocket?.close(WEBSOCKET_NORMAL_SHUTDOWN_CODE, "Client-side shutdown")
    webSocket = null
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    Timber.d("Received message from server: $text")

    val webSocketResponse = WebSocketResponse.adapter.fromJson(text) ?: WebSocketResponse.Error("Malformed json: $text")
    Timber.d("Successfully parsed response")

    when (webSocketResponse) {
      is WebSocketResponse.Error -> Timber.e(webSocketResponse.errorMessage)
      is WebSocketResponse.NewMessage -> {
        saveMessage(webSocketResponse)
      }
    }
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    Timber.e("Websocket failed: ${t.message}")
  }

  fun sendMessage(request: WebSocketRequest) {
    val json = WebSocketRequest.toJson(request)
    Timber.d("Sending message through websocket: $json")

    webSocket?.send(json)
  }

  private fun saveMessage(webSocketResponse: WebSocketResponse.NewMessage) {
    ioScope.launch(ioDispatcher) {
      chatDao.insertMessage(webSocketResponse.messageDto.toMessageEntity(myId = profileDataSource.getMyId()))
    }
  }

  companion object {
    const val WEBSOCKET_NORMAL_SHUTDOWN_CODE = 1000
  }

}