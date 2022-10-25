package com.sillyapps.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ImageButton(
  onClick: () -> Unit,
  painter: Painter,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = onClick,
    modifier = modifier
  ) {
    Image(
      painter = painter,
      contentDescription = null
    )
  }
}