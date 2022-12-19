package com.lofigroup.seeyau.features.chat.model

import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.sillyapps.core_time.formatIfNeeded
import com.sillyapps.core_time.intervalToString

sealed interface UIMessageType {
  object Plain: UIMessageType
  object Like: UIMessageType
  object Contact: UIMessageType
  class Video(
    val mediaItem: MediaItem
  ): UIMessageType
  class Audio(
    val mediaItem: MediaItem,
    val duration: Long
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
    is MessageType.Audio -> UIMessageType.Audio(mediaItem = MediaItem.fromUri(uri), duration = duration)
    is MessageType.Video -> UIMessageType.Video(mediaItem = MediaItem.fromUri(uri))
  }
}