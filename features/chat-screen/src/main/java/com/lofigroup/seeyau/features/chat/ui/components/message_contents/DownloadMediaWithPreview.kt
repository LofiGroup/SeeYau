package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.lofigroup.seeyau.common.ui.getFormattedFileSize
import com.lofigroup.seeyau.domain.chat.models.MediaData
import com.lofigroup.seeyau.features.chat.model.base64ToBitmap
import com.lofigroup.seeyau.features.chat.ui.composition_locals.LocalChatMediaDownloader
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import timber.log.Timber

@Composable
fun BoxScope.DownloadMediaWithPreview(
  base64Image: String,
  messageId: Long,
  mediaData: MediaData
) {
  val fileDownloader = LocalChatMediaDownloader.current

  val bitmap = remember {
    base64ToBitmap(base64Image)
  }

  val loadingProgress by fileDownloader.subscribe(messageId).collectAsState(initial = DownloadProgress())

  DisposableEffect(Unit) {
    onDispose {
      fileDownloader.unsubscribe(messageId)
    }
  }

  RemoteImage(
    model = bitmap,
    shape = MaterialTheme.shapes.small,
    contentScale = ContentScale.FillWidth,
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = LocalSpacing.current.extraSmall)
  )

  if (loadingProgress.isStarted) {
    LaunchedEffect(key1 = loadingProgress) {
      Timber.e("New progress: ${loadingProgress.progress}")
    }
    CircularProgressIndicator(
      progress = loadingProgress.progress,
      modifier = Modifier.align(Alignment.Center),
      color = LocalExtendedColors.current.disabled
    )
  } else {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .align(Alignment.Center)
    ) {
      IconButton(
        onClick = { fileDownloader.startDownload(messageId) },
      ) {
        Icon(
          imageVector = Icons.Filled.Download,
          contentDescription = null,
          modifier = Modifier
            .size(LocalSize.current.small)
        )
      }

      Spacer(modifier = Modifier.height(LocalSpacing.current.small))

      Text(
        text = getFormattedFileSize(sizeInBytes = mediaData.fileSize),
        style = MaterialTheme.typography.h6
      )
    }
  }
}