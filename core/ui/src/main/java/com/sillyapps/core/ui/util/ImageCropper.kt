package com.sillyapps.core.ui.util

import android.graphics.Bitmap
import android.os.Build
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options

fun getDefaultImageCropperOptions() = options {
  setGuidelines(CropImageView.Guidelines.ON)
  setCropShape(CropImageView.CropShape.RECTANGLE)

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
    setOutputCompressFormat(Bitmap.CompressFormat.WEBP_LOSSY)
  else
    setOutputCompressFormat(Bitmap.CompressFormat.WEBP)
}