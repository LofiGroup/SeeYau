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
import com.lofigroup.seayau.common.ui.components.UserIcon
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
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    UserIcon(
      imageUri = user.imageUrl,
      isOnline = user.isOnline,
      onClick = { onUserSelected(user.id) }
    )

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
        user = UserItemUIModel.getPreviewModel(isOnline = true),
        onUserSelected = {}
      )
    }
  }

}