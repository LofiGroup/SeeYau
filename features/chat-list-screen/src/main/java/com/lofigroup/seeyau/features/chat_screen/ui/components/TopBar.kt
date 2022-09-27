package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.R as CommonR
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  totalNewMessages: Int,
  onUpButtonClick: () -> Unit
) {
  Box(
    Modifier
      .fillMaxWidth()
  ) {
    IconButton(
      onClick = onUpButtonClick
    ) {
      Icon(
        painter = painterResource(id = CommonR.drawable.ic_arrow_1_icon),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.CenterStart),
      )
    }
    Text(
      text = stringResource(id = R.string.chats),
      style = MaterialTheme.typography.h5,
      modifier = Modifier.align(Alignment.Center)
    )

    TextLabel(
      text = "+${totalNewMessages}",
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = LocalSpacing.current.medium)
    )
  }
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