package com.lofigroup.seayau.common.ui.components.specific

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sillyapps.core.ui.components.RemoteImage

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BigImage(
  isVisible: Boolean,
  onDismiss: () -> Unit,
  url: String?
) {
  if (isVisible) {
    Dialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false
      ),
      onDismissRequest = onDismiss
    ) {
      Box(
        modifier = Modifier
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onDismiss
          )
      ) {
        RemoteImage(
          model = url,
          modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f),
          onClick = {}
        )
      }
    }
  }
}