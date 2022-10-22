package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.components.DefaultTopBar
import com.lofigroup.seayau.common.ui.components.UpButton
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.TextLabel

@Composable
fun TopBar(
  totalNewMessages: Int,
  onUpButtonClick: () -> Unit
) {
  DefaultTopBar(
    title = stringResource(id = R.string.chats),
    leftContent = { UpButton(onClick = onUpButtonClick) },
    rightContent = {
      TextLabel(
        text = "+${totalNewMessages}",
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