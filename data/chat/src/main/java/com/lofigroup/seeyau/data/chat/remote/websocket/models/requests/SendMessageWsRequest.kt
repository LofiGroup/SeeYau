package com.lofigroup.seeyau.data.chat.remote.websocket.models.requests

import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.SendMessageDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SendMessageWsRequest(
  val type: String = "chat_message",

  @Json(name = "local_id")
  val localId: Long,
  val message: String,
  @Json(name = "chat_id")
  val chatId: Long
) : WebSocketRequest

fun MessageEntity.toSendMessageRequest() = SendMessageWsRequest(
  localId = id,
  message = message,
  chatId = chatId
)

fun SendMessageDto.toSendMessageWsRequest() = SendMessageWsRequest(
  localId = localId,
  message = message,
  chatId = chatId
)