package com.sillyapps.core.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.sillyapps.core.ui.theme.LocalExtendedColors

@Composable
fun OneLiner(
  text: String,
  style: TextStyle = MaterialTheme.typography.subtitle2
) {
  Text(
    text = text,
    style = style,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    color = LocalExtendedColors.current.lightBackground
  )
}