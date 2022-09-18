package com.lofigroup.seeyau.data.chat.remote.websocket.models.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkChatAsRead(
  val type: String = "mark_chat_as_read",
  @Json(name = "chat_id")
  val chatId: Long
): WebSocketRequest