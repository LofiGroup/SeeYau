package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.common.ui.R
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyle
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun BoxScope.LikeMessage(
  style: ChatMessageStyle,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.align(style.alignment)
  ) {
    Image(painter = painterResource(id = R.drawable.ic_like), contentDescription = null)
    Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))
    Text(
      text = stringResource(id = R.string.sent_you_like),
      style = MaterialTheme.typography.h6
    )
  }
}