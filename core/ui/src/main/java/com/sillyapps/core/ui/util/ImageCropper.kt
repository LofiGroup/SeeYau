package com.sillyapps.core.ui.util

import android.graphics.Bitmap
import android.os.Build
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

fun getDefaultImageCropperOptions() = CropImageContractOptions(
  uri = null,
  cropImageOptions = CropImageOptions(
    guidelines = CropImageView.Guidelines.ON,
    cropShape = CropImageView.CropShape.RECTANGLE,
    outputCompressFormat =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
      Bitmap.CompressFormat.WEBP_LOSSY
    else
      Bitmap.CompressFormat.WEBP
  )
)