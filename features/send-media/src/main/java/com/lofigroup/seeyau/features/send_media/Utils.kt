package com.lofigroup.seeyau.features.send_media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun initTempUri(context: Context): Uri {
  val tempImagesDir = File(context.filesDir, context.getString(R.string.temp_images))
  tempImagesDir.mkdir()
  val tempImage = File(tempImagesDir, context.getString(R.string.temp_image))

  return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempImage)
}