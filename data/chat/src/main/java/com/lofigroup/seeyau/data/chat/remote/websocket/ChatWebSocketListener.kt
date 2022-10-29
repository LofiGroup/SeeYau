package com.lofigroup.seeyau.data.chat.remote.websocket

import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.backend_api.websocket.WebSocketChannelListener
import com.lofigroup.seeyau.data.chat.ChatDataHandler
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.MarkChatAsRead
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.WebSocketRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.*
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ChatWebSocketListener @Inject constructor(
  private val webSocketChannel: WebSocketChannel,
  private val chatDao: ChatDao,
  private val chatApi: ChatApi,
  private val ioScope: CoroutineScope,
  private val ioDispatcher: CoroutineDispatcher,
  private val profileDataHandler: ProfileDataHandler,
  private val chatDataHandler: ChatDataHandler,
): WebSocketChannelListener {

  init {
    webSocketChannel.registerListener(TAG, this)
  }

  override fun onMessage(message: String) {
    val response = ChatWebSocketResponse.adapter.fromJson(message) ?: ErrorWsResponse("Malformed json: $message")

    when (response) {
      is ErrorWsResponse -> Timber.e(response.errorMessage)
      is NewMessageWsResponse -> {
        chatDataHandler.saveMessage(response)
      }
      is ChatIsReadWsResponse -> {
        chatDataHandler.onChatIsRead(response)
      }
      is UserOnlineStateChangedWsResponse -> {
        ioScope.launch(ioDispatcher) {
          if (response.userId != profileDataHandler.getMyId()) {
            profileDataHandler.pullUserData(response.userId)
          }
        }
      }
      is NewChatIsCreatedWsResponse -> {
        chatDataHandler.pullChatData(response.chatId)
      }
      is MessageIsReceivedResponse -> {
        chatDataHandler.saveLocalMessage(response)
      }
      else -> {}
    }
  }

  fun sendMessage(request: WebSocketRequest) {
    val json = WebSocketRequest.toJson(request)

    webSocketChannel.sendMessage(TAG, json)
  }

  fun markChatAsRead(chatId: Long) {
    val json = WebSocketRequest.toJson(MarkChatAsRead(chatId = chatId))
    webSocketChannel.sendMessage(TAG, json)
  }

  companion object {
    const val TAG = "chat"
  }

}