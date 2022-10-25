package com.sillyapps.core.ui.util

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.conditional(condition: Boolean, modify: Modifier.() -> Modifier) =
  if (condition) modify() else this

fun Modifier.universalBackground(background: Any) = run {
  when (background) {
    is Color -> background(background)
    is Brush -> background(background)
    else -> this
  }
}