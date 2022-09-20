package com.sillyapps.core.ui.components

import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SurfacePlaceholder(
  color: Color = MaterialTheme.colors.background,
  contentColor: Color = contentColorFor(color),
  elevation: Dp = 0.dp,
  content: @Composable () -> Unit
) {
  val absoluteElevation = LocalAbsoluteElevation.current + elevation

  CompositionLocalProvider(
    LocalContentColor provides contentColor,
    LocalAbsoluteElevation provides absoluteElevation
  ) {
    content()
  }
}