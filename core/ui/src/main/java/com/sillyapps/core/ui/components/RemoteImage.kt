package com.sillyapps.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.Transformation
import com.sillyapps.core.ui.R

@Composable
fun RemoteImage(
  model: Any?,
  modifier: Modifier = Modifier,
  placeholderResId: Int? = null,
  errorPlaceholderResId: Int = R.drawable.ic_baseline_broken_image_24,
  onClick: () -> Unit = NOT_CLICKABLE,
  shape: Shape = CircleShape,
  contentScale: ContentScale = ContentScale.Crop,
  transformations: List<Transformation> = emptyList()
) {

  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(model)
      .crossfade(true)
      .transformations(transformations)
      .build(),
    placeholder = placeholderResId?.let { painterResource(id = errorPlaceholderResId) },
    contentScale = contentScale,
    contentDescription = null,
    error = painterResource(id = errorPlaceholderResId),
    modifier = modifier
      .clip(shape)
      .clickable(enabled = onClick != NOT_CLICKABLE, onClick = onClick)
  )

}

private val NOT_CLICKABLE = {}