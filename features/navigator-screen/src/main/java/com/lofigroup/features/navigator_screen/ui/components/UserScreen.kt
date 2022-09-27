package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
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
fun ColumnScope.UserScreen(
  user: UserItemUIModel,
  isInFullScreenMode: Boolean,
  onToggleFullScreenMode: () -> Unit,
  onMoreButtonClicked: () -> Unit,
  onShowChat: () -> Unit,
  onGoToChat: (Long) -> Unit
) {

  Box(
    modifier = Modifier.weight(1f)
  ) {
    UserScreenControls(
      userId = user.id,
      isInFullScreenMode = isInFullScreenMode,
      onToggleFullScreenMode = onToggleFullScreenMode,
      onMoreButtonClicked = onMoreButtonClicked,
      onShowChat = onShowChat,
      onGoToChat = onGoToChat
    )
  }

}

@Composable
fun BoxScope.UserScreenControls(
  userId: Long,
  isInFullScreenMode: Boolean,
  onToggleFullScreenMode: () -> Unit,
  onMoreButtonClicked: () -> Unit,
  onShowChat: () -> Unit,
  onGoToChat: (Long) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Spacer(modifier = Modifier.weight(1f))
    ControlWrapper {
      ControlItem(
        resId = if (isInFullScreenMode) R.drawable.ic_oi_fullscreen_exit
                else R.drawable.ic_oi_fullscreen_enter,
        onClick = onToggleFullScreenMode
      )
    }
    ControlWrapper {
      ControlItem(
        resId = R.drawable.ic_more_1_icon,
        onClick = onMoreButtonClicked
      )
    }
    ControlWrapper {
      ControlItem(
        resId = R.drawable.ic_stm_1_icon,
        onClick = onShowChat,
        alignment = Alignment.CenterStart
      )
      ControlItem(
        resId = R.drawable.ic_sfm_2_icon,
        onClick = { onGoToChat(userId) }
      )
    }
  }
}

@Composable
fun ControlWrapper(
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    Modifier
      .fillMaxWidth()
      .padding(LocalSpacing.current.medium)
  ) {
    content()
  }
}

@Composable
fun BoxScope.ControlItem(
  resId: Int,
  onClick: () -> Unit,
  alignment: Alignment = Alignment.CenterEnd,
) {
  IconButton(
    onClick = onClick,
    modifier = Modifier
      .align(alignment)
      .size(LocalSize.current.iconSize)
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
      Column() {
        UserScreen(
          user = UserItemUIModel.getPreviewModel(),
          isInFullScreenMode = true,
          onToggleFullScreenMode = {  },
          onMoreButtonClicked = {  },
          onShowChat = {  }) {
        }
      }
    }
  }
}