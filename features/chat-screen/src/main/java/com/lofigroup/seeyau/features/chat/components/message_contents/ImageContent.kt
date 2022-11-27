package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize

@Composable
fun ImageContent(
  content: MessageType.Image
) {
  RemoteImage(
    model = content.uri,
    shape = MaterialTheme.shapes.small,
    modifier = Modifier.size(width = LocalSize.current.large, height = LocalSize.current.veryLarge)
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
      )
    }
  }
}