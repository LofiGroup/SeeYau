package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserOnlineStateChangedWsResponse(
  @Json(name = "user_id")
  val userId: Long
): WebSocketResponse
