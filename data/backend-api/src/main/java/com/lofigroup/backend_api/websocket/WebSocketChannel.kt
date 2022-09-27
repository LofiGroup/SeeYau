package com.lofigroup.backend_api.websocket

import com.lofigroup.backend_api.websocket.models.WebSocketResponse
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.launch
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

@AppScope
class WebSocketChannel @Inject constructor(
  private val client: OkHttpClient,
) : WebSocketListener() {

  private val wsChatRequest =
    Request.Builder().url("wss://${SeeYauApiConstants.baseUrl}/ws/main/")
      .header("origin", "wss://${SeeYauApiConstants.baseUrl}")
      .build()

  private val webSocket: WebSocket = client.newWebSocket(wsChatRequest, this)

  private val listeners = mutableMapOf<String, WebSocketChannelListener>()

  fun disconnect() {
    webSocket.close(WEBSOCKET_NORMAL_SHUTDOWN_CODE, "Client-side shutdown")
  }

  fun registerListener(type: String, listener: WebSocketChannelListener) {
    listeners[type] = listener
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    val response = WebSocketResponse.adapter.fromJson(text)
    if (response == null) {
      Timber.e("Malformed data")
      return
    }

    val listener = listeners[response.type]
    if (listener == null) {
      Timber.e("Unknown response type: ${response.type}")
      return
    }

    listener.onMessage(response.data)
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    Timber.e("Websocket failed: ${t.message}")
  }

  fun sendMessage(request: String) {
    Timber.d("Sending message through websocket: $request")
    webSocket.send(request)
  }

  companion object {
    const val WEBSOCKET_NORMAL_SHUTDOWN_CODE = 1000
  }

}