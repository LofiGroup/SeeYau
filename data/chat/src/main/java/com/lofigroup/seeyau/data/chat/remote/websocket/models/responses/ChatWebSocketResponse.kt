package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed interface ChatWebSocketResponse {
  companion object {
    val adapter: JsonAdapter<ChatWebSocketResponse> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(ChatWebSocketResponse::class.java, "type")
        .withSubtype(NewMessageWsResponse::class.java, "new_message")
        .withSubtype(ErrorWsResponse::class.java, "error")
        .withSubtype(UserOnlineStateChangedWsResponse::class.java, "online_status_changed")
        .withSubtype(NewChatIsCreatedWsResponse::class.java, "new_chat_is_created")
        .withSubtype(ChatIsReadWsResponse::class.java, "chat_is_read")
    ).build().adapter(ChatWebSocketResponse::class.java)
  }
}
