package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ImageContent(
  content: UIMessageType.Image
) {
  RemoteImage(
    model = content.uri,
    shape = MaterialTheme.shapes.small,
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(9 / 16f)
      .padding(bottom = LocalSpacing.current.extraSmall)
  )
}

@Preview
@Composable
fun ImageMessagePreview() {
  AppTheme {
    Box(Modifier.fillMaxSize()) {
      BaseMessage(
        message = getPreviewPrivateMessage(type = MessageType.Image(uri = "")),
        style = LocalChatMessageStyles.current.myMessageStyle,
        isCurrentItem = false,
      )
    }
  }
}