package com.lofigroup.seeyau.features.send_media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun SendMediaItem(
  onClick: () -> Unit,
  imageVector: ImageVector,
  title: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(LocalSpacing.current.small)
  ) {
    IconButton(onClick = onClick) {
      Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier.size(LocalSize.current.small)
      )
    }
    Text(
      text = title,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.subtitle2
    )
  }
}

@Preview
@Composable
fun SendMediaItemPreview() {
  AppTheme {
    Surface() {
      SendMediaItem(
        onClick = {  },
        imageVector = Icons.Filled.Photo,
        title = "Photo"
      )
    }
  }
}