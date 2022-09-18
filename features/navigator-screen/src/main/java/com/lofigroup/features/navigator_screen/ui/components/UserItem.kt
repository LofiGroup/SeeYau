package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.RemoteImage

import com.lofigroup.seayau.common.ui.R as common_res

@Composable
fun UserItem(
  user: UserItemUIModel
) {
  Surface {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .padding(vertical = 6.dp)
        .padding(start = 4.dp, end = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      RemoteImage(
        model = user.imageUrl,
        modifier = Modifier
          .size(40.dp)
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = user.name,
            style = MaterialTheme.typography.h6
          )

          if (user.isNear) {
            Row(
              modifier = Modifier.padding(start = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                  .size(10.dp)
              )
              Text(
                text = stringResource(id = R.string.near),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 4.dp)
              )
            }
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
          painter = painterResource(id = common_res.drawable.ic_baseline_more_vert_24),
          contentDescription = null,
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }

}

@Preview
@Composable
fun UserItemPreview() {

  AppTheme {
    UserItem(
      user = UserItemUIModel(
        imageUrl = "",
        isNear = true,
        name = "Random"
      )
    )
  }

}