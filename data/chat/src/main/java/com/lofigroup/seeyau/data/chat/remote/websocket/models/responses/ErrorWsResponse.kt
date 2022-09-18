package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ErrorWsResponse(
  @Json(name = "error_message")
  val errorMessage: String
) : WebSocketResponse