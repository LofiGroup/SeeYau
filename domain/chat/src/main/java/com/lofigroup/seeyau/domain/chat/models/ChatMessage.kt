package com.lofigroup.seeyau.domain.chat.models

data class ChatMessage(
  val id: Long,
  val author: Long,
  val createdIn: Long,
  val message: String,
  val status: MessageStatus,
  val type: MessageType,
  val mediaUri: String?
)

enum class MessageStatus {
  SENDING, SENT, RECEIVED, READ
}

enum class MessageType {
  PLAIN, AUDIO, VIDEO, IMAGE, CONTACT, LIKE
}
