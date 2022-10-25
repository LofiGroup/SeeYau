package com.sillyapps.core.ui.util

import androidx.compose.ui.Modifier

fun Modifier.conditional(condition: Boolean, modify: Modifier.() -> Modifier) =
  if (condition) modify() else this