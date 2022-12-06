package com.sillyapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.Transformation
import com.sillyapps.core.ui.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.conditional
import com.valentinilk.shimmer.shimmer

@Composable
fun RemoteImage(
  model: Any?,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = NOT_CLICKABLE,
  shape: Shape = CircleShape,
  contentScale: ContentScale = ContentScale.Crop,
  transformations: List<Transformation> = emptyList(),
  loadingPlaceholder: @Composable () -> Unit = { DefaultLoadingPlaceholder() },
  errorPlaceholder: @Composable () -> Unit = { DefaultErrorPlaceholder() },
) {
  val imageRequest =
    if (model is ImageRequest) model
    else ImageRequest.Builder(LocalContext.current)
      .data(model)
      .crossfade(true)
      .transformations(transformations)
      .build()

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    SubcomposeAsyncImage(
      model = imageRequest,
      contentScale = contentScale,
      contentDescription = null,
      loading = { loadingPlaceholder() },
      error = { errorPlaceholder() },
      modifier = Modifier
        .fillMaxSize()
        .clip(shape)
        .conditional(onClick != NOT_CLICKABLE) {
          clickable(onClick = onClick)
        }
    )
  }
}

@Composable
private fun DefaultLoadingPlaceholder() {
  Spacer(modifier = Modifier
    .fillMaxSize()
    .shimmer()
    .background(LocalExtendedColors.current.disabled))
}

@Composable
private fun DefaultErrorPlaceholder() {
  Spacer(modifier = Modifier
    .fillMaxSize()
    .background(LocalExtendedColors.current.disabled))
}

private val NOT_CLICKABLE = {}

@Preview
@Composable
fun RemoteImagePreview() {
  Surface(modifier = Modifier.fillMaxSize()) {
    Box(contentAlignment = Alignment.Center) {
      RemoteImage(
        model = "",
        modifier = Modifier.size(200.dp)
      )
    }
  }
}