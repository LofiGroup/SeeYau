package com.lofigroup.seeyau.data.chat.remote.http.models

import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.Draft
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatUpdatesDto(
  val id: Long,
  @Json(name = "partner_id")
  val partnerId: Long,
  @Json(name = "new_messages")
  val newMessages: List<ChatMessageDto>,
  @Json(name = "created_in")
  val createdIn: Long?,
  @Json(name = "last_visited")
  val lastVisited: Long,
  @Json(name = "partner_last_visited")
  val partnerLastVisited: Long
)

fun ChatUpdatesDto.toChatEntity() =
  ChatEntity(
    id = id,
    partnerId = partnerId,
    lastVisited = lastVisited,
    partnerLastVisited = partnerLastVisited,
    draft = Draft(message = "", 0L),
    createdIn = createdIn ?: 0L
  )
