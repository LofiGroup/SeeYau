package com.lofigroup.seeyau.data.chat.local.models

import android.content.Context
import android.net.Uri
import com.lofigroup.seeyau.domain.chat.models.MessageType

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
    MessageTypeEntity.VIDEO -> {
      val videoExtra = extra?.let { VideoExtra.adapter.fromJson(it) } ?: VideoExtra("", "")
      MessageType.Video(uri = videoExtra.uri, thumbnailUri = videoExtra.thumbnailUri)
    }
    else -> {
      val uri = extra?.let { MediaExtra.adapter.fromJson(it)?.uri } ?: ""
      if (this == MessageTypeEntity.AUDIO) MessageType.Audio(uri = uri)
      else MessageType.Image(uri = uri)
    }
  }
}

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

