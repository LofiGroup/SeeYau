package com.lofigroup.seeyau.features.chat.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.telephony.mbms.FileInfo
import android.util.Base64
import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.domain.chat.models.MediaData
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.sillyapps.core_time.formatIfNeeded
import com.sillyapps.core_time.intervalToString

sealed interface UIMessageType {
  object Plain: UIMessageType
  object Like: UIMessageType
  object Contact: UIMessageType
  class Video(
    val mediaItem: MediaItem = MediaItem.EMPTY,
    val mediaData: MediaData = MediaData(),
    val aspectRatio: Float = 1f,
    val duration: Long = 0L,
    val preview: String = ""
  ): UIMessageType
  class Audio(
    val mediaItem: MediaItem = MediaItem.EMPTY,
    val mediaData: MediaData = MediaData(),
    val duration: Long = 0L
  ): UIMessageType
  class Image(
    val mediaData: MediaData = MediaData(),
    val aspectRatio: Float = 1f,
    val preview: String = ""
  ): UIMessageType
}

fun MessageType.toUIMessageType(): UIMessageType {
  return when (this) {
    is MessageType.Contact -> UIMessageType.Contact
    is MessageType.Like -> UIMessageType.Like
    is MessageType.Plain -> UIMessageType.Plain

    is MessageType.Image -> UIMessageType.Image(
      mediaData = mediaData,
      aspectRatio = previewData.width.toFloat() / previewData.height,
      preview = previewData.previewBase64
    )
    is MessageType.Audio -> UIMessageType.Audio(
      mediaItem = MediaItem.fromUri(mediaData.uri),
      mediaData = mediaData,
      duration = duration
    )
    is MessageType.Video -> UIMessageType.Video(
      mediaItem = MediaItem.fromUri(mediaData.uri),
      mediaData = mediaData,
      aspectRatio = previewData.width.toFloat() / previewData.height,
      duration = duration,
      preview = previewData.previewBase64
    )
  }
}

fun base64ToBitmap(base64: String): Bitmap? {
  return if (base64.isNotEmpty()) {
    val bytes = Base64.decode(base64, Base64.DEFAULT)
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
  } else null
}