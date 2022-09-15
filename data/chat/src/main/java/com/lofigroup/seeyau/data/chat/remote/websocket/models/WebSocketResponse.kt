package com.lofigroup.seeyau.data.chat.remote.websocket.models

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed class WebSocketResponse {
  @JsonClass(generateAdapter = true)
  class NewMessage(
    @Json(name = "message")
    val messageDto: ChatMessageDto
  ) : WebSocketResponse()

  @JsonClass(generateAdapter = true)
  class Error(
    @Json(name = "error_message")
    val errorMessage: String
  ) : WebSocketResponse()

  companion object {
    val adapter: JsonAdapter<WebSocketResponse> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(WebSocketResponse::class.java, "type")
        .withSubtype(NewMessage::class.java, "chat_message")
        .withSubtype(Error::class.java, "error")
    ).build().adapter(WebSocketResponse::class.java)
  }
}