package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.getFormattedFileSize
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.base64ToBitmap
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.ui.composition_locals.LocalChatMediaDownloader
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import timber.log.Timber

@Composable
fun ImageContent(
  content: UIMessageType.Image,
  message: UIChatMessage,
  onImageClick: (String) -> Unit,
) {
  Box(
    modifier = Modifier.
        aspectRatio(content.aspectRatio)
  ) {
    if (content.mediaData.isSavedLocally) {
      RemoteImage(
        model = content.mediaData.uri,
        shape = MaterialTheme.shapes.small,
        onClick = { onImageClick(content.mediaData.uri) },
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = LocalSpacing.current.extraSmall)
      )
    }
    else {
      DownloadMediaWithPreview(
        base64Image = content.preview,
        messageId = message.id,
        mediaData = content.mediaData
      )
    }

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
        chatMessage = getPreviewMessage(type = MessageType.Image()),
        onImageClick = {}
      )
    }
  }
}