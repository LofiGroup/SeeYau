package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UserItem(
  user: UserItemUIModel,
  onUserSelected: (Long) -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(LocalSpacing.current.extraSmall)
  ) {
    Box {
      UserIcon(
        imageUri = user.imageUrl,
        isOnline = user.isOnline,
        onClick = { onUserSelected(user.id) },
        isSelected = user.isSelected,
        modifier = Modifier
          .padding(top = LocalSpacing.current.small)
          .align(Alignment.BottomCenter)
      )

      if (user.hasNewMessages) {
        Image(
          painter = painterResource(id = R.drawable.ic_sfm_indicator),
          contentDescription = null,
          modifier = Modifier
            .align(alignment = Alignment.TopEnd)
        )
      }
    }

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

    Text(
      text = user.name,
      style = MaterialTheme.typography.h6,
    )
  }

}

@Preview
@Composable
fun UserItemPreview() {

  AppTheme {
    Surface() {
      UserItem(
        user = UserItemUIModel.getPreviewModel(isOnline = true, isSelected = true),
        onUserSelected = {}
      )
    }
  }

}