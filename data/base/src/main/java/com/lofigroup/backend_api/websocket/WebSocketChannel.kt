package com.lofigroup.backend_api.websocket

import com.lofigroup.backend_api.websocket.models.WebSocketResponse
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.sillyapps.core.di.AppScope
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

@AppScope
class WebSocketChannel @Inject constructor(
  private val client: OkHttpClient
) : WebSocketListener() {

  private val wsChatRequest =
    Request.Builder().url("wss://${SeeYauApiConstants.baseUrl}/ws/main/")
      .header("origin", "wss://${SeeYauApiConstants.baseUrl}")
      .build()

  private var mWebSocket: WebSocket? = null

  private val listeners = mutableMapOf<String, WebSocketChannelListener>()

  fun connect() {
    if (mWebSocket != null) return
    mWebSocket = client.newWebSocket(wsChatRequest, this)
  }

  fun disconnect() {
    mWebSocket?.close(WEBSOCKET_NORMAL_SHUTDOWN_CODE, "Client-side shutdown")
    mWebSocket = null
  }

  fun registerListener(type: String, listener: WebSocketChannelListener) {
    listeners[type] = listener
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    Timber.e("Received message: $text")
    val response = WebSocketResponse.fromJson(text)

    if (response == null) {
      Timber.e("Malformed data")
      return
    }

    val listener = listeners[response.type]
    if (listener == null) {
      Timber.e("Unknown response type: ${response.type}")
      return
    }

    try {
      listener.onMessage(response.data)
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    Timber.e("Websocket failed: ${t.message}")

    mWebSocket = client.newWebSocket(wsChatRequest, this)
  }

  fun sendMessage(type: String, request: String) {
    val message = "{\"type\":\"$type\",\"data\":$request}"
    Timber.d("Sending message through websocket: $message")
    try {
      mWebSocket?.send(message)
    } catch (e: Exception) {
      Timber.e("Exception while sending message through websocket: $message")
    }
  }

  companion object {
    const val WEBSOCKET_NORMAL_SHUTDOWN_CODE = 1000
  }

}