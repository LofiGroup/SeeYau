package com.lofigroup.backend_api.websocket.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class WebSocketResponse(
  val type: String,
  val data: String
) {
  companion object {
    val adapter = Moshi.Builder().build().adapter(WebSocketResponse::class.java)
  }
}
