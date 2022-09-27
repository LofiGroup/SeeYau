package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSpacing

import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun UserItem(
  user: UserItemUIModel,
  onUserSelected: (Long) -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable {
        onUserSelected(user.id)
      }
  ) {
    Box() {
      RemoteImage(
        model = user.imageUrl,
        placeholderResId = CommonR.drawable.ic_baseline_account_circle_24,
        errorPlaceholderResId = CommonR.drawable.ic_baseline_account_circle_24,
        onClick = { onUserSelected(user.id) },
        modifier = Modifier
          .size(80.dp)
          .padding(LocalSpacing.current.small)
      )

      if (user.isOnline) {
        Image(
          painter = painterResource(id = R.drawable.ic_online_big),
          contentDescription = null,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = LocalSpacing.current.medium)
        )
      }
    }

    Text(
      text = user.name,
      style = MaterialTheme.typography.h6
    )
  }

}

@Preview
@Composable
fun UserItemPreview() {

  AppTheme {
    Surface() {
      UserItem(
        user = UserItemUIModel.getPreviewModel(),
        onUserSelected = {}
      )
    }
  }

}