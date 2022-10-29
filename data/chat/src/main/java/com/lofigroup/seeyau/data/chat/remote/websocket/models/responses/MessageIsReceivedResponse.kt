package com.lofigroup.seeyau.data.chat.remote.websocket.models.responses

import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageIsReceivedResponse(
  @Json(name = "local_id")
  val localId: Long,
  @Json(name = "real_id")
  val realId: Long,
  @Json(name = "created_in")
  val createdIn: Long
): ChatWebSocketResponse