package com.lofigroup.seeyau.data.chat.remote.websocket.models.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SendMessageWsRequest(
  val type: String = "chat_message",
  val message: String,
  @Json(name = "chat_id")
  val chatId: Long
) : WebSocketRequest