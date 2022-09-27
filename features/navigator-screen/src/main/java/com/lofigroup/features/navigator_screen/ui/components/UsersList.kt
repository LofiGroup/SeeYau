package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.ImageWrap
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UsersList(
  nearbyUsers: List<UserItemUIModel>,
  metUsers: List<UserItemUIModel>,
  onUserSelected: (Long) -> Unit
) {
  LazyRow(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(bottom = LocalSpacing.current.medium)
  ) {
    item {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(LocalSize.current.userIconSize)
      ) {
        Image(
          painterResource(id = R.drawable.ic_nearby_icon),
          contentDescription = null
        )
        Text(
          text = stringResource(id = R.string.nearby),
          style = MaterialTheme.typography.h6
        )
      }

      if (nearbyUsers.isEmpty()) {
        ImageWrap(
          resourceId = R.drawable.ic_placeholder,
          modifier = Modifier.size(LocalSize.current.userIconSize)
        )
      }
    }

    items(
      items = nearbyUsers
    ) { user ->
      UserItem(
        user = user,
        onUserSelected = onUserSelected
      )
    }

    item {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(LocalSize.current.userIconSize)
      ) {
        Image(
          painterResource(id = R.drawable.ic_met_icon),
          contentDescription = null
        )
        Text(
          text = stringResource(id = R.string.met),
          style = MaterialTheme.typography.h6
        )
      }

      if (metUsers.isEmpty()) {
        ImageWrap(
          resourceId = R.drawable.ic_placeholder,
          modifier = Modifier.size(LocalSize.current.userIconSize)
        )
      }
    }

    items(
      items = metUsers
    ) { user ->
      UserItem(
        user = user,
        onUserSelected = onUserSelected
      )
    }
  }
}

@Preview
@Composable
fun PreviewUsersList() {
  val users = listOf(
    UserItemUIModel.getPreviewModel(),
    UserItemUIModel.getPreviewModel()
  )

  AppTheme {
    Surface(
      modifier = Modifier
        .background(LocalExtendedColors.current.backgroundGradient)
        .fillMaxSize()
    ) {
      Column() {
        Spacer(modifier = Modifier.weight(1f))
        UsersList(
          nearbyUsers = users,
          metUsers = listOf(),
          onUserSelected = {}
        )
      }
    }
  }
}