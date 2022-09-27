package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewChatIsCreatedWsResponse(
  @Json(name = "chat_id")
  val chatId: Long
): ChatWebSocketResponse
