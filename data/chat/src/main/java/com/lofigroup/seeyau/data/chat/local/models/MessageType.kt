package com.lofigroup.seeyau.data.chat.local.models

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import com.lofigroup.seeyau.domain.chat.models.MessageType
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

fun MessageTypeEntity.toMessageType(extra: String?, context: Context): MessageType {
  return when (this) {
    MessageTypeEntity.PLAIN -> MessageType.Plain
    MessageTypeEntity.CONTACT -> MessageType.Contact()
    MessageTypeEntity.VIDEO, MessageTypeEntity.AUDIO, MessageTypeEntity.IMAGE -> {
      val uri = extractMediaUri(extra)
      when (this) {
        MessageTypeEntity.VIDEO -> resolveVideoType(rawUri = uri, context = context)
        MessageTypeEntity.AUDIO -> MessageType.Audio(
          uri = uri,
          duration = getAudioFileDuration(uri, context)
        )
        else -> MessageType.Image(uri = uri,)
      }
    }
  }
}

fun resolveVideoType(rawUri: String, context: Context): MessageType.Video {
  val mmr = MediaMetadataRetriever()
  val uri = Uri.parse(rawUri)

  if (uri.scheme == "content") mmr.setDataSource(context, uri)
  else mmr.setDataSource(rawUri)

  val width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 1
  val height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 1
  val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L

  mmr.release()

  return MessageType.Video(
    uri = rawUri,
    width = width,
    height = height,
    duration = duration
  )
}

fun extractMediaUri(extra: String?): String {
  return extra?.let { MediaExtra.adapter.fromJson(it)?.uri } ?: ""
}

fun getAudioFileDuration(rawUri: String, context: Context): Long {
  if (rawUri.isBlank()) return 0L

  return try {
    val mmr = MediaMetadataRetriever()

    val uri = Uri.parse(rawUri)
    if (uri.scheme == "content") mmr.setDataSource(context, uri)
    else mmr.setDataSource(rawUri)

    val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "0"

    mmr.release()
    duration.toLong()
  } catch (e: Exception) {
    0L
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

