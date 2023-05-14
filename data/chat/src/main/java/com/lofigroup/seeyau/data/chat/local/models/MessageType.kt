package com.lofigroup.seeyau.data.chat.local.models

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import com.lofigroup.seeyau.domain.chat.models.MediaData
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.domain.chat.models.PreviewData
import timber.log.Timber

enum class MessageTypeEntity {
  PLAIN, AUDIO, IMAGE, CONTACT, VIDEO
}


fun toMessageType(type: String): MessageTypeEntity {
  return when (type) {
    "audio" -> MessageTypeEntity.AUDIO
    "video" -> MessageTypeEntity.VIDEO
    "image" -> MessageTypeEntity.IMAGE
    "contact" -> MessageTypeEntity.CONTACT
    else -> MessageTypeEntity.PLAIN
  }
}

fun MessageTypeEntity.toMessageType(extra: String?): MessageType {
  return when (this) {
    MessageTypeEntity.PLAIN -> MessageType.Plain
    MessageTypeEntity.CONTACT -> MessageType.Contact()

    else -> {
      if (extra == null) return MessageType.Plain

      when (this) {
        MessageTypeEntity.VIDEO -> {
          val videoExtra = VideoExtra.adapter.fromJson(extra)!!
          videoExtra.toVideoType()
        }
        MessageTypeEntity.IMAGE -> {
          val imageExtra = ImageExtra.adapter.fromJson(extra)!!
          imageExtra.toImageType()
        }
        MessageTypeEntity.AUDIO -> {
          val audioExtra = AudioExtra.adapter.fromJson(extra)!!
          audioExtra.toAudioType()
        }

        else -> MessageType.Plain
      }
    }
  }
}

fun AudioExtra.toAudioType() = MessageType.Audio(
  mediaData = fileInfo.toMediaData(),
  duration = duration,
)

fun VideoExtra.toVideoType() = MessageType.Video(
  mediaData = fileInfo.toMediaData(),
  duration = duration,
  previewData = preview.toPreviewData()
)

fun ImageExtra.toImageType() = MessageType.Image(
  mediaData = fileInfo.toMediaData(),
  previewData = preview.toPreviewData()
)

fun FileInfo.toMediaData() = MediaData(
  uri = uri,
  fileSize = fileSize,
  isSavedLocally = !uri.startsWith("http")
)

fun ThumbnailPreview.toPreviewData() = PreviewData(
  previewBase64 = base64,
  height = height,
  width = width
)

fun resolveMessageType(uri: String?, context: Context): MessageTypeEntity {
  if (uri == null) return MessageTypeEntity.PLAIN

  val mimeType = context.contentResolver.getType(Uri.parse(uri)) ?: return MessageTypeEntity.PLAIN

  return when {
    mimeType.startsWith("image") -> MessageTypeEntity.IMAGE
    mimeType.startsWith("audio") -> MessageTypeEntity.AUDIO
    mimeType.startsWith("video") -> MessageTypeEntity.VIDEO
    else -> MessageTypeEntity.PLAIN
  }
}

