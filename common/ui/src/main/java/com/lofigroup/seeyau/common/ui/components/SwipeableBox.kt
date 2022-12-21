package com.lofigroup.seeyau.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.util.dpToPx
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableBox(
  swipeableState: SwipeableState<Int>,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  height: Dp = 300.dp,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val heightPx = remember {
    context.dpToPx(height).toFloat()
  }

  val anchors = mapOf(0f to 0, heightPx to 1)
  val coroutineScope = rememberCoroutineScope()

  Box(modifier = modifier
    .fillMaxSize()
    .swipeable(
      state = swipeableState,
      anchors = anchors,
      orientation = Orientation.Vertical,
      thresholds = { _, _ -> FractionalThreshold(0.5f) }
    )
    .clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = null,
      onClick = {
        coroutineScope.launch { swipeableState.animateTo(1) }
      }
    )
    .offset { IntOffset(x = 0, y = swipeableState.offset.value.roundToInt()) },

    contentAlignment = Alignment.BottomCenter
  ) {
    LaunchedEffect(Unit) {
      swipeableState.snapTo(1)
      swipeableState.animateTo(0)
    }

    LaunchedEffect(swipeableState.isAnimationRunning) {
      if (!swipeableState.isAnimationRunning && swipeableState.currentValue == 1) {
        onDismiss()
      }
    }

    Box(
    ) {
      content()
    }

  }
}