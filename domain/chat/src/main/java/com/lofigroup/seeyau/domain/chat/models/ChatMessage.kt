package com.lofigroup.seeyau.domain.chat.models

sealed class ChatMessage(
  val id: Long,
  val author: Long,
  val createdIn: Long,
  val status: MessageStatus
) {

  class PlainMessage(
    val message: String,
    id: Long,
    author: Long,
    createdIn: Long,
    status: MessageStatus
  ): ChatMessage(
    id = id,
    author = author,
    createdIn = createdIn,
    status = status
  )

  class LikeMessage(
    author: Long,
    createdIn: Long,
    status: MessageStatus
  ): ChatMessage(
    id = 0L,
    author = author,
    createdIn = createdIn,
    status = status
  )
}

enum class MessageStatus {
  SENDING, SENT, RECEIVED, READ
}