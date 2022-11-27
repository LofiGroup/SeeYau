package com.lofigroup.seeyau.data.chat.remote.http.models

import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.chat.local.models.toMessageType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessageDto(
  val id: Long,
  val message: String,
  @Json(name = "created_in")
  val createdIn: Long,
  @Json(name = "chat_id")
  val chatId: Long,
  val author: Long,
  @Json(name = "media_uri")
  val mediaUri: String?,
  @Json(name = "message_type")
  val type: String
)

fun ChatMessageDto.toMessageEntity(myId: Long, readIn: Long) = MessageEntity(
  id = id,
  message = message,
  createdIn = createdIn,
  author = if (author == myId) 0 else author,
  chatId = chatId,
  isRead = createdIn < readIn,
  extra = mediaUri,
  type = toMessageType(type)
)