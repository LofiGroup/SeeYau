package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  totalNewMessages: Int,
  onUpButtonClick: () -> Unit
) {
  DefaultTopBar(
    title = stringResource(id = R.string.chats),
    leftContent = { UpButton(onClick = onUpButtonClick) },
    rightContent = {
      if (totalNewMessages > 0)
        TextLabel(
          text = "+${totalNewMessages}",
          modifier = Modifier.padding(end = LocalSpacing.current.small)
        )
    }
  )
}

@Preview
@Composable
fun TopBarPreview() {
  AppTheme() {
    Surface() {
      TopBar(
        totalNewMessages = 1,
        onUpButtonClick = {}
      )
    }
  }
}