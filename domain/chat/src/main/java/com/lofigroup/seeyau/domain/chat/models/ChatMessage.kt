package com.lofigroup.seeyau.domain.chat.models

import kotlinx.coroutines.flow.Flow

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

sealed class MessageType() {
  object Plain: MessageType()
  object Like: MessageType()
  class Contact: MessageType()
  class Audio(
    val mediaData: MediaData = MediaData(),
    val duration: Long = 0L
    ): MessageType()
  class Video(
    val mediaData: MediaData = MediaData(),
    val previewData: PreviewData = PreviewData(),
    val duration: Long = 0L,
  ): MessageType()
  class Image(
    val mediaData: MediaData = MediaData(),
    val previewData: PreviewData = PreviewData()
  ): MessageType()
}

data class MediaData(
  val uri: String = "",
  val fileSize: Long = 1,
  val isSavedLocally: Boolean = true
)

data class PreviewData(
  val previewBase64: String = "",
  val width: Int = 1,
  val height: Int = 1
)
