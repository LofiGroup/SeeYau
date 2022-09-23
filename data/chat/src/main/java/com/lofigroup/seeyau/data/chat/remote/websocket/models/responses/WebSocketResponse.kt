package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed interface WebSocketResponse {
  companion object {
    val adapter: JsonAdapter<WebSocketResponse> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(WebSocketResponse::class.java, "response_type")
        .withSubtype(NewMessageWsResponse::class.java, "chat_message")
        .withSubtype(ErrorWsResponse::class.java, "error")
        .withSubtype(UserOnlineStateChangedWsResponse::class.java, "online_status_changed")
    ).build().adapter(WebSocketResponse::class.java)
  }
}
