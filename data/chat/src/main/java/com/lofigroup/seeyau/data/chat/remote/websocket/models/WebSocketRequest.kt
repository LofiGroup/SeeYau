package com.lofigroup.seeyau.data.chat.remote.websocket.models

import com.squareup.moshi.*

sealed class WebSocketRequest {

  @JsonClass(generateAdapter = true)
  class SendMessage(
    val type: String = "chat_message",
    val message: String,
    @Json(name = "chat_id")
    val chatId: Long
  ) : WebSocketRequest()

  companion object {
    private val moshi = Moshi.Builder().build()
    private val sendMessageAdapter = moshi.adapter(SendMessage::class.java)

    fun toJson(webSocketRequest: WebSocketRequest): String {
      return when (webSocketRequest) {
        is SendMessage -> sendMessageAdapter.toJson(webSocketRequest)
      }
    }
  }

}