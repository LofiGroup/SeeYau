package com.lofigroup.seeyau.domain.chat.models

data class ChatMessage(
  val id: Long,
  val author: Long,
  val chatId: Long,
  val createdIn: Long,
  val message: String,
  val status: MessageStatus,
  val type: MessageType
)

enum class MessageStatus {
  SENDING, SENT, RECEIVED, READ
}

sealed class MessageType(
  val uri: String? = null
) {
  object Plain: MessageType()
  object Like: MessageType()
  class Contact: MessageType()
  class Audio(uri: String, val duration: Long): MessageType(uri)
  class Video(
    uri: String,
    val width: Int = 1,
    val height: Int = 1,
    val duration: Long = 0L
  ): MessageType(uri)
  class Image(
    uri: String,
    val width: Int = 1,
    val height: Int = 1
  ): MessageType(uri)
}
