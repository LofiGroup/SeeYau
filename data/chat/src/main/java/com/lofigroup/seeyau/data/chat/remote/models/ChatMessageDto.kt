package com.lofigroup.seeyau.data.chat.remote.models

import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessageDto(
  val id: Long,
  val message: String,
  @Json(name = "created_in")
  val createdIn: Long,
  val author: Long
)

fun ChatMessageDto.toMessageEntity(chatId: Long, myId: Long) = MessageEntity(
  id = id,
  message = message,
  createdIn = createdIn,
  author = if (author == myId) 0 else author,
  chatId = chatId
)