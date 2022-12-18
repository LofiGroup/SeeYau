package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.ui.providers.LocalChatMessageStyles
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ImageContent(
  content: UIMessageType.Image,
  createdIn: String
) {
  Box() {
    RemoteImage(
      model = content.uri,
      shape = MaterialTheme.shapes.small,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(9 / 16f)
        .padding(bottom = LocalSpacing.current.extraSmall)
    )

    Text(
      text = createdIn,
      style = MaterialTheme.typography.caption,
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
      ImageContent(content = UIMessageType.Image(uri = ""), createdIn = "00:00")
    }
  }
}