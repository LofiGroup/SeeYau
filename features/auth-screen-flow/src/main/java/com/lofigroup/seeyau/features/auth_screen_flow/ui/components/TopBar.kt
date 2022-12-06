package com.lofigroup.seeyau.features.auth_screen_flow.ui.components

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.UpButton

@Composable
fun TopBar(
  onUpButtonClick: () -> Unit = NO_UP_BUTTON
) {
  DefaultTopBar(
    title = "",
    leftContent = {
      if (onUpButtonClick != NO_UP_BUTTON)
        UpButton(onClick = onUpButtonClick)
    },
    modifier = Modifier.statusBarsPadding()
  )
}

private val NO_UP_BUTTON = {}