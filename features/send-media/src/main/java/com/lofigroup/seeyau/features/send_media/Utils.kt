package com.lofigroup.seeyau.features.send_media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File

fun initTempUri(context: Context): Uri {
  val tempImagesDir = File(context.filesDir, context.getString(R.string.temp_images))
  tempImagesDir.mkdir()
  val tempImage = File(tempImagesDir,  "${System.currentTimeMillis()}.webp")
  val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempImage)

  return uri
}

val documentMimeTypes = arrayOf(
  "application/pdf",
  "application/zip",
  "application/msword",
  "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
  "application/epub+zip",
  "application/vnd.oasis.opendocument.text",
  "application/vnd.oasis.opendocument.spreadsheet",
  "application/vnd.oasis.opendocument.presentation",
  "application/vnd.ms-powerpoint",
  "application/vnd.openxmlformats-officedocument.presentationml.presentation",
  "application/vnd.rar",
  "text/plain",
  "application/x-7z-compressed"
)