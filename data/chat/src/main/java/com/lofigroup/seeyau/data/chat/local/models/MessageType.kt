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

fun MessageTypeEntity.toMessageType(): MessageType {
  return MessageType.valueOf(this.toString())
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

