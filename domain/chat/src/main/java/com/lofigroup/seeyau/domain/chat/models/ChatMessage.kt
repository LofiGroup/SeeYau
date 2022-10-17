package com.lofigroup.seeyau.domain.chat.models

sealed class ChatMessage(
  val id: Long,
  val author: Long,
  val createdIn: Long,
  val isRead: Boolean
) {

  class PlainMessage(
    val message: String,
    id: Long,
    author: Long,
    createdIn: Long,
    isRead: Boolean
  ): ChatMessage(
    id = id,
    author = author,
    createdIn = createdIn,
    isRead = isRead
  )

  class LikeMessage(
    author: Long,
    createdIn: Long,
    isRead: Boolean
  ): ChatMessage(
    id = 0L,
    author = author,
    createdIn = createdIn,
    isRead = isRead
  )

}