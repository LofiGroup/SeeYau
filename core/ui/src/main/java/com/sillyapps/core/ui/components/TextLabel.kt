package com.sillyapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TextLabel(
  text: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(MaterialTheme.shapes.medium)
      .background(LocalExtendedColors.current.secondaryGradient)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.h6,
      modifier = Modifier
        .padding(horizontal = LocalSpacing.current.extraSmall)
    )
  }
}