package com.sillyapps.core.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import timber.log.Timber

fun Modifier.conditional(condition: Boolean, modify: Modifier.() -> Modifier) =
  if (condition) modify() else this

fun Modifier.universalBackground(background: Any) = run {
  when (background) {
    is Color -> background(background)
    is Brush -> background(background)
    else -> this
  }
}

fun <T> LazyListScope.gridItems(
  data: List<T>,
  columnCount: Int,
  modifier: Modifier = Modifier,
  content: @Composable (T) -> Unit
) {
  val rowsCount = ceilingDivision(data.size, columnCount)

  items(rowsCount) { rowIndex ->
    Row(
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = modifier
    ) {
      for (columnIndex in 0 until columnCount) {
        val itemIndex = rowIndex * columnCount + columnIndex

        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
        ) {
          if (itemIndex < data.size)
            content(data[itemIndex])
        }
      }
    }
  }
}