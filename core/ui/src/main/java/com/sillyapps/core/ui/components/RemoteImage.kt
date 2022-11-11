package com.sillyapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.Transformation
import com.sillyapps.core.ui.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.conditional

@Composable
fun RemoteImage(
  model: Any?,
  modifier: Modifier = Modifier,
  placeholderResId: Int? = null,
  errorPlaceholderResId: Int = R.drawable.ic_baseline_broken_image_24,
  onClick: () -> Unit = NOT_CLICKABLE,
  shape: Shape = CircleShape,
  contentScale: ContentScale = ContentScale.Crop,
  transformations: List<Transformation> = emptyList(),
) {
  SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(model)
      .crossfade(true)
      .transformations(transformations)
      .build(),
    contentScale = contentScale,
    contentDescription = null,
    loading = {
      CircularProgressIndicator(
        modifier = Modifier
          .heightIn(min = 0.dp, max = LocalSize.current.medium)
          .fillMaxSize(0.8f)
          .padding(LocalSpacing.current.small)
      )
    },
    error = {
      Spacer(modifier = Modifier
        .fillMaxSize()
        .background(LocalExtendedColors.current.disabled.copy(alpha = 0.6f)))
    },
    modifier = modifier
      .clip(shape)
      .conditional(onClick != NOT_CLICKABLE) {
        clickable(onClick = onClick)
      }
  )


}

private val NOT_CLICKABLE = {}