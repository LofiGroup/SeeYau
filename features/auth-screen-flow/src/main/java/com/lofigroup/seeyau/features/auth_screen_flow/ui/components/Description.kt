package com.lofigroup.seeyau.features.auth_screen_flow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun Description(
  title: String,
  caption: String
) {
  Text(
    text = title,
    style = MaterialTheme.typography.h4,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = LocalSpacing.current.bigMedium)
  )

  Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

  Text(
    text = caption,
    style = MaterialTheme.typography.subtitle2.copy(color = LocalExtendedColors.current.disabled),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = LocalSpacing.current.bigMedium)
  )
}