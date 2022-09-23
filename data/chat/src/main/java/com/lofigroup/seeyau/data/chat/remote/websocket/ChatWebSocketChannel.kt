package com.lofigroup.seeyau.data.chat.remote.websocket

import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.MarkChatAsRead
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.WebSocketRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.*
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ChatWebSocketChannel @Inject constructor(
  private val client: OkHttpClient,
  private val chatDao: ChatDao,
  private val userDao: UserDao,
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

    val webSocketResponse = WebSocketResponse.adapter.fromJson(text) ?: ErrorWsResponse("Malformed json: $text")
    Timber.d("Successfully parsed response")

    when (webSocketResponse) {
      is ErrorWsResponse -> Timber.e(webSocketResponse.errorMessage)
      is NewMessageWsResponse -> {
        saveMessage(webSocketResponse)
      }
      is ChatIsReadWsResponse -> {
        ioScope.launch(ioDispatcher) {
          if (webSocketResponse.userId == profileDataSource.getMyId()) {
            chatDao.updateChatLastVisited(webSocketResponse.chatId, webSocketResponse.readIn)
          } else {
            chatDao.updateChatPartnerLastVisited(webSocketResponse.chatId, webSocketResponse.readIn)
          }
        }
      }
      is UserOnlineStateChangedWsResponse -> {
        ioScope.launch(ioDispatcher) {
          if (webSocketResponse.userId != profileDataSource.getMyId()) {

          }
        }
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

  fun markChatAsRead(chatId: Long) {
    val json = WebSocketRequest.toJson(MarkChatAsRead(chatId = chatId))
    webSocket?.send(json)
  }

  private fun saveMessage(webSocketResponse: NewMessageWsResponse) {
    ioScope.launch(ioDispatcher) {
      chatDao.insertMessage(webSocketResponse.messageDto.toMessageEntity(myId = profileDataSource.getMyId()))
    }
  }

  companion object {
    const val WEBSOCKET_NORMAL_SHUTDOWN_CODE = 1000
  }

}