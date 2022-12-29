package com.sillyapps.core.ui.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import timber.log.Timber
import kotlin.math.abs

@Composable
fun ZoomableBox(
  modifier: Modifier = Modifier,
  minScale: Float = 1f,
  maxScale: Float = 5f,
  content: @Composable BoxScope.() -> Unit
) {
  var scale by remember { mutableStateOf(1f) }
  var offsetX by remember { mutableStateOf(0f) }
  var offsetY by remember { mutableStateOf(0f) }
  var size by remember { mutableStateOf(IntSize.Zero) }

  Box(
    modifier = modifier
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
        translationX = offsetX
        translationY = offsetY
      }
      .onSizeChanged { size = it }
      .pointerInput(Unit) {
        detectTransformGestures { centroid, pan, zoom, _ ->
          scale = maxOf(minScale, minOf(scale * zoom, maxScale))

          val maxX = (size.width * (scale - 1)) / 2
          val minX = -maxX
          val maxY = (size.height * (scale - 1)) / 2
          val minY = -maxY

          var realZoom = zoom

          if (zoom > 1f && abs(maxScale - scale) < 0.00001) {
            realZoom = 1f
          }
          val resultX = offsetX * realZoom - (centroid.x - size.center.x) * (realZoom - 1) + pan.x * scale
          val resultY = offsetY * realZoom - (centroid.y - size.center.y) * (realZoom - 1) + pan.y * scale

          offsetX = maxOf(minX, minOf(maxX, resultX))
          offsetY = maxOf(minY, minOf(maxY, resultY))
        }
      }
  ) {
    content()
  }
}