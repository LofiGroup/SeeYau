package com.lofigroup.seeyau.data.chat.remote.http.models

import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

@JsonClass(generateAdapter = true)
data class SendMessageDto(
  @Json(name = "local_id")
  val localId: Long,
  val message: String,
  @Json(name = "chat_id")
  val chatId: Long,
  @Json(name = "message_type")
  val type: String,
  val mediaUri: String?
) {
  companion object {
    val adapter: JsonAdapter<SendMessageDto> = Moshi.Builder().build().adapter(SendMessageDto::class.java)
  }
}

fun MessageEntity.toSendMessageDto() = SendMessageDto(
  localId = id,
  message = message,
  chatId = chatId,
  type = type.toString().lowercase(),
  mediaUri = mediaUri
)

fun SendMessageDto.toForm(): MutableMap<String, RequestBody> {
  return mutableMapOf(
    Pair("local_id", localId.toString().toRequestBody("text/plain".toMediaType())),
    Pair("message", message.toRequestBody("text/plain".toMediaType())),
    Pair("chat_id", chatId.toString().toRequestBody("text/plain".toMediaType())),
    Pair("message_type", type.toRequestBody("text/plain".toMediaType())),
  )
}
