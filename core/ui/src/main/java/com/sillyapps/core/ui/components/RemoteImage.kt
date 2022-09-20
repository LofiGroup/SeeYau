package com.sillyapps.core.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sillyapps.core.ui.R

@Composable
fun RemoteImage(
  model: Any?,
  modifier: Modifier = Modifier,
  placeholderResId: Int = R.drawable.ic_baseline_broken_image_24,
  errorPlaceholderResId: Int = R.drawable.ic_baseline_broken_image_24,
  onClick: () -> Unit = {}
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(model)
      .crossfade(true)
      .build(),
    placeholder = painterResource(id = placeholderResId),
    contentScale = ContentScale.Crop,
    contentDescription = null,
    error = painterResource(id = errorPlaceholderResId),
    modifier = modifier
      .clip(CircleShape)
      .clickable { onClick() }
  )

}