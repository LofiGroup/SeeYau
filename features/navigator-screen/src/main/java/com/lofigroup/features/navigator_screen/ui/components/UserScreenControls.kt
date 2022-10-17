package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UserScreenControls(
  user: UserItemUIModel,
  isInFullScreenMode: Boolean,
  onLikeButtonClick: (Boolean, Long) -> Unit,
  onToggleFullScreenMode: () -> Unit,
  onMoreButtonClicked: () -> Unit,
  onGoToChat: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .padding(end = LocalSpacing.current.medium),
  ) {
    Spacer(modifier = Modifier.weight(1f))

    LikeButton(
      isPressed = user.isLikedByMe,
      likesCount = user.likesCount,
      onClick = { onLikeButtonClick(it, user.id) }
    )

    ControlItem(
      resId = if (isInFullScreenMode) R.drawable.ic_oi_fullscreen_exit
      else R.drawable.ic_oi_fullscreen_enter,
      onClick = onToggleFullScreenMode
    )
    ControlItem(
      resId = R.drawable.ic_more_1_icon,
      onClick = onMoreButtonClicked
    )

    ControlItem(
      resId = R.drawable.ic_sfm_2_icon,
      onClick = { onGoToChat(user.id) }
    )
  }
}

@Composable
fun LikeButton(
  isPressed: Boolean,
  likesCount: Int,
  onClick: (Boolean) -> Unit
) {
  Column(
    modifier = Modifier.clickable { onClick(!isPressed) },
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (isPressed) {
      Icon(
        painter = painterResource(id = R.drawable.ic_like_enabled),
        contentDescription = null,
        modifier = Modifier.size(LocalSize.current.medium)
      )
    } else {
      Icon(
        painter = painterResource(id = R.drawable.ic_like_disabled),
        contentDescription = null,
        modifier = Modifier.size(LocalSize.current.medium)
      )
    }

    Text(
      text = likesCount.toString(),
      style = MaterialTheme.typography.h6
    )
  }
}

@Composable
fun ControlItem(
  resId: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = onClick,
    modifier = modifier
      .padding(vertical = LocalSpacing.current.medium)
      .size(LocalSize.current.medium)
  ) {
    Image(
      painter = painterResource(id = resId),
      contentDescription = null
    )
  }
}

@Preview
@Composable
fun PreviewUserScreen() {
  AppTheme {
    Surface {
      Row {
        Spacer(modifier = Modifier.weight(1f))
        UserScreenControls(
          user = UserItemUIModel.getPreviewModel(),
          isInFullScreenMode = true,
          onToggleFullScreenMode = { },
          onMoreButtonClicked = { },
          onGoToChat = {},
          onLikeButtonClick = { state, id -> }
        )
      }
    }
  }
}