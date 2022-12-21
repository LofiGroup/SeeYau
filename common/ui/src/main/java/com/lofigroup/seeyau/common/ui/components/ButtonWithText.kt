package com.lofigroup.seeyau.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.common.ui.theme.appButtonColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.conditional
import com.sillyapps.core.ui.util.universalBackground

@Composable
fun ButtonWithText(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  backgroundColor: Any = MaterialTheme.colors.primary
) {
  Button(
    onClick = onClick,
    colors = appButtonColors(),
    enabled = enabled,
    contentPadding = PaddingValues(all = 0.dp),
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .conditional(enabled) { universalBackground(backgroundColor) },
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        style = MaterialTheme.typography.h3,
        modifier = Modifier
          .padding(LocalSpacing.current.medium)
      )
    }

  }
}

@Preview
@Composable
fun ButtonWithTextPreview() {
  AppTheme {
    Surface() {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ButtonWithText(text = "Next", onClick = {})
      }

    }
  }
}