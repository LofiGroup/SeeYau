package com.lofigroup.seeyau.data.chat.remote.websocket.models.requests

import com.squareup.moshi.Moshi

sealed interface WebSocketRequest {

  companion object {
    private val moshi = Moshi.Builder().build()
    private val sendMessageAdapter = moshi.adapter(SendMessageWsRequest::class.java)
    private val markChatAsReadAdapter = moshi.adapter(MarkChatAsRead::class.java)

    fun toJson(webSocketRequest: WebSocketRequest): String {
      return when (webSocketRequest) {
        is SendMessageWsRequest -> sendMessageAdapter.toJson(webSocketRequest)
        is MarkChatAsRead -> markChatAsReadAdapter.toJson(webSocketRequest)
        else -> { "" }
      }
    }
  }

}