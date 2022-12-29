package com.lofigroup.seeyau.features.chat.ui.components

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.components.ZoomableBox

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CloseUpImage(
  imageUri: String?,
  visible: Boolean,
  onDismiss: () -> Unit
) {
  AnimatedVisibility(
    visible = visible,
    enter = scaleIn(),
    exit = scaleOut() + slideOutHorizontally { it / 2 }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(if (visible) MaterialTheme.colors.background else Color.Transparent)
        .systemBarsPadding()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onDismiss
        )
    ) {
      if (visible)
        DefaultTopBar(
          leftContent = { UpButton(onClick = onDismiss) },
          modifier = Modifier.zIndex(1f)
        )

      ZoomableBox(modifier = Modifier.weight(1f)) {
        RemoteImage(
          model = imageUri,
          contentScale = ContentScale.FillWidth,
          shape = RectangleShape,
          elevation = 0.dp,
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
        )
      }
    }
  }
  if (visible)
    BackHandler {
      onDismiss()
    }
}