package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatIsReadWsResponse(
  @Json(name = "chat_id")
  val chatId: Long,
  @Json(name = "user_id")
  val userId: Long,
  @Json(name = "read_in")
  val readIn: Long
) : ChatWebSocketResponse

fun ChatIsReadWsResponse.toChatEvent() = ChatIsRead(
  chatId = chatId
)