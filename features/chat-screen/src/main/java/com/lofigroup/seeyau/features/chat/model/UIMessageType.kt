package com.lofigroup.seeyau.features.chat.model

import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.domain.chat.models.MessageType

sealed interface UIMessageType {
  object Plain: UIMessageType
  object Like: UIMessageType
  object Contact: UIMessageType
  class Video(
    val mediaItem: MediaItem,
    val thumbnailUri: String,
    val isPlaying: Boolean = false
  ): UIMessageType
  class Audio(
    val mediaItem: MediaItem,
  ): UIMessageType
  class Image(
    val uri: String
  ): UIMessageType
}

fun MessageType.toUIMessageType(): UIMessageType {
  val uri = uri ?: ""
  return when (this) {
    is MessageType.Contact -> UIMessageType.Contact
    is MessageType.Like -> UIMessageType.Like
    is MessageType.Plain -> UIMessageType.Plain

    is MessageType.Image -> UIMessageType.Image(uri)
    is MessageType.Audio -> UIMessageType.Audio(MediaItem.fromUri(uri))
    is MessageType.Video -> UIMessageType.Video(mediaItem = MediaItem.fromUri(uri), thumbnailUri = thumbnailUri)
  }
}