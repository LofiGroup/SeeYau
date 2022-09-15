package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.lofigroup.domain.navigator.model.User
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.sillyapps.core.ui.components.RemoteImage
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ChatItem(
  chat: Chat,
  onClick: (Long) -> Unit
) {
  Surface(
    modifier = Modifier.clickable { onClick(chat.id) }
  ) {
    Row(modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min)
      .padding(vertical = 6.dp)
      .padding(start = 4.dp, end = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      RemoteImage(url = chat.partner.imageUrl)

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = chat.partner.name,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(end = 4.dp)
          )

          if (chat.partner.isNear) {
            Icon(
              painter = painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
              contentDescription = null,
              tint = MaterialTheme.colors.primary,
              modifier = Modifier
                .size(10.dp)
            )
          }
        }
        Text(
          text = "Let's shine!",
          style = MaterialTheme.typography.caption,
          color = MaterialTheme.colors.error,
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      IconButton(onClick = { }) {
        Icon(
          painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
          contentDescription = null,
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }
}

@Preview
@Composable
fun ChatItemPreview() {
  val item = Chat(
    id = 0,
    partner = User(
      id = 0,
      name = "Honda",
      imageUrl = "",
      isNear = true,
      lastConnection = 0
    ),
    messages = listOf()
  )

  AppTheme {
    ChatItem(
      chat = item,
      onClick = {}
    )
  }
}