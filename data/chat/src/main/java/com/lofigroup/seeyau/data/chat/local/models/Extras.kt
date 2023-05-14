package com.lofigroup.seeyau.data.chat.local.models

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.io.IOException

sealed interface Extra

@JsonClass(generateAdapter = true)
data class FileInfo(
  val uri: String,
  @Json(name = "file_size")
  val fileSize: Long
): Extra

@JsonClass(generateAdapter = true)
data class VideoExtra(
  @Json(name = "file_info")
  val fileInfo: FileInfo,
  val preview: ThumbnailPreview,
  val duration: Long
): Extra {
  companion object {
    val adapter: JsonAdapter<VideoExtra> = Moshi.Builder().build().adapter(VideoExtra::class.java)
  }
}

@JsonClass(generateAdapter = true)
data class ImageExtra(
  @Json(name = "file_info")
  val fileInfo: FileInfo,
  val preview: ThumbnailPreview
): Extra {
  companion object {
    val adapter: JsonAdapter<ImageExtra> = Moshi.Builder().build().adapter(ImageExtra::class.java)
  }
}

@JsonClass(generateAdapter = true)
data class AudioExtra(
  @Json(name = "file_info")
  val fileInfo: FileInfo,
  val duration: Long,
): Extra {
  companion object {
    val adapter: JsonAdapter<AudioExtra> = Moshi.Builder().build().adapter(AudioExtra::class.java)
  }
}

object NoExtra: Extra

@JsonClass(generateAdapter = true)
data class BaseExtra(
  @Json(name = "file_info")
  val fileInfo: FileInfo
) {
  companion object {
    val adapter: JsonAdapter<BaseExtra> = Moshi.Builder().build().adapter(BaseExtra::class.java)
  }
}

@JsonClass(generateAdapter = true)
data class ThumbnailPreview(
  val base64: String,
  val width: Int,
  val height: Int
)

fun updateExtraUri(newUri: String, extra: String, messageType: MessageTypeEntity): String? {
  return when (messageType) {
    MessageTypeEntity.PLAIN, MessageTypeEntity.CONTACT -> {
      null
    }
    MessageTypeEntity.AUDIO -> {
      val audioExtra = AudioExtra.adapter.fromJson(extra)!!
      val fileInfo = audioExtra.fileInfo
      AudioExtra.adapter.toJson(audioExtra.copy(fileInfo = fileInfo.copy(uri = newUri)))
    }
    MessageTypeEntity.IMAGE -> {
      val imageExtra = ImageExtra.adapter.fromJson(extra)!!
      val fileInfo = imageExtra.fileInfo
      ImageExtra.adapter.toJson(imageExtra.copy(fileInfo = fileInfo.copy(uri = newUri)))
    }
    MessageTypeEntity.VIDEO -> {
      val videoExtra = VideoExtra.adapter.fromJson(extra)!!
      val fileInfo = videoExtra.fileInfo
      VideoExtra.adapter.toJson(videoExtra.copy(fileInfo = fileInfo.copy(uri = newUri)))
    }
  }
}

fun extractExtraFromUri(uri: String?, type: MessageTypeEntity, context: Context): String? {
  if (uri == null) return null

  return when (type) {
    MessageTypeEntity.AUDIO -> {
      AudioExtra.adapter.toJson(extractAudioExtra(rawUri = uri, context = context))
    }
    MessageTypeEntity.IMAGE -> ImageExtra.adapter.toJson(extractImageExtra(rawUri = uri, context = context))
    MessageTypeEntity.VIDEO -> VideoExtra.adapter.toJson(extractVideoExtra(rawUri = uri, context = context))
    else -> null
  }
}

fun extractMediaUri(extra: String?): String? {
  if (extra == null) return null

  return try {
    BaseExtra.adapter.fromJson(extra)?.fileInfo?.uri
  } catch (e: IOException) {
    null
  }
}

fun extractVideoExtra(rawUri: String, context: Context): VideoExtra? {
  val mmr = MediaMetadataRetriever()
  val uri = Uri.parse(rawUri)

  if (uri.scheme != "content") return null

  mmr.setDataSource(context, uri)

  val width =
    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 1
  val height =
    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 1
  val duration =
    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L

  mmr.release()

  return VideoExtra(
    fileInfo = FileInfo(
      uri = rawUri,
      fileSize = 1
    ),
    duration = duration,
    preview = ThumbnailPreview(
      width = width,
      height = height,
      base64 = ""
    )
  )
}

fun extractImageExtra(rawUri: String, context: Context): ImageExtra {
  val uri = Uri.parse(rawUri)

  val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
  val inputStream = context.contentResolver.openInputStream(uri)
  BitmapFactory.decodeStream(inputStream, null, options)

  val width = options.outWidth
  val height = options.outHeight
  inputStream?.close()

  return ImageExtra(
    fileInfo = FileInfo(
      uri = rawUri,
      fileSize = 1
    ),
    preview = ThumbnailPreview(
      width = width,
      height = height,
      base64 = ""
    )
  )
}

fun extractAudioExtra(rawUri: String, context: Context): AudioExtra {
  val mmr = MediaMetadataRetriever()

  val uri = Uri.parse(rawUri)
  mmr.setDataSource(context, uri)

  val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "0"

  return AudioExtra(
    fileInfo = FileInfo(
      uri = rawUri,
      fileSize = 1
    ),
    duration = duration.toLong()
  )
}
