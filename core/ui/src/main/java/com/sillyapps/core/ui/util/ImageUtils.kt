package com.sillyapps.core.ui.util

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.executeBlocking
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

fun extractBitmapFromUri(context: Context, uri: String?): Bitmap? {
  if (uri == null) return null

  val loader = Coil.imageLoader(context)

  val result = loader.executeBlocking(
    ImageRequest.Builder(context)
      .data(uri)
      .transformations(CircleCropTransformation())
      .build())

  return result.drawable?.toBitmap()
}