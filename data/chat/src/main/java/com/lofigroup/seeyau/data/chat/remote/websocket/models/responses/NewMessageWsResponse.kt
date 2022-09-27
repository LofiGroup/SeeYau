package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NewMessageWsResponse(
  @Json(name = "message")
  val messageDto: ChatMessageDto
) : ChatWebSocketResponse