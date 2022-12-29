package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ImageContent(
  content: UIMessageType.Image,
  message: UIChatMessage,
  onImageClick: (String) -> Unit,
) {
  Box() {
    RemoteImage(
      model = content.uri,
      shape = MaterialTheme.shapes.small,
      onClick = { onImageClick(content.uri) },
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = LocalSpacing.current.extraSmall)
    )

    MessageDate(
      message = message,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(LocalSpacing.current.small)
    )
  }
}

@Preview
@Composable
fun ImageMessagePreview() {
  AppTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      ChatMessageItem(
        chatMessage = getPreviewMessage(type = MessageType.Image(uri = "")),
        onImageClick = {}
      )
    }
  }
}