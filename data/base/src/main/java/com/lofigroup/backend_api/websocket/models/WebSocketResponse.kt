package com.lofigroup.backend_api.websocket.models


data class WebSocketResponse(
  val type: String,
  val data: String
) {
  companion object {
    private val regex = "\"type\": \"(.+)\".*\"data\": (\\{.+)\\}".toRegex()

    fun fromJson(json: String): WebSocketResponse? {
      val response = regex.find(json) ?: return null

      val type = response.groupValues[1]
      val data = response.groupValues[2]

      return WebSocketResponse(type, data)
    }
  }
}