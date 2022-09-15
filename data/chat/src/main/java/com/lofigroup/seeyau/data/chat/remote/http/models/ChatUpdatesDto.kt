package com.lofigroup.seeyau.data.chat.remote.http.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatUpdatesDto(
  val id: Long,
  @Json(name = "partner")
  val partnerId: Long,
  @Json(name = "new_messages")
  val newMessages: List<ChatMessageDto>
)
