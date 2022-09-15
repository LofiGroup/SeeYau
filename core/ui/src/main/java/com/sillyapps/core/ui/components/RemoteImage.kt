package com.sillyapps.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.sillyapps.core.ui.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RemoteImage(
  url: String?
) {
  GlideImage(
    imageModel = url,
    contentScale = ContentScale.FillBounds,
    alignment = Alignment.Center,
    success = { imageState ->
      val drawable = imageState.drawable

      if (drawable != null) {
        Image(
          bitmap = drawable.toBitmap().asImageBitmap(),
          contentDescription = null,
          modifier = Modifier.fillMaxSize()
        )
      }
    },
    failure = {
      Image(
        painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.Gray),
        modifier = Modifier.fillMaxSize().clip(CircleShape)
      )
    },
    previewPlaceholder = R.drawable.ic_baseline_broken_image_24,
    modifier = Modifier
      .size(64.dp)
      .clip(CircleShape)
  )
}