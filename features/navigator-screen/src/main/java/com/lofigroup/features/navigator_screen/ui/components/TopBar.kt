package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.previewProfile
import com.lofigroup.seayau.common.ui.components.DefaultTopBar
import com.lofigroup.seayau.common.ui.components.LikesLabel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  profile: Profile,
  newMessagesCount: Int,
  onSettingsButtonClick: () -> Unit,
  onCloudButtonClick: () -> Unit
) {
  DefaultTopBar(
    title = "",
    leftContent = {
      ProfileSettingsButton(
        profile = profile,
        onClick = onSettingsButtonClick
      )
    },
    rightContent = {
      Box {
        Image(
          painter = painterResource(id = R.drawable.ic_cloud_icon),
          contentDescription = null,
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .clickable { onCloudButtonClick() }
        )

        if (newMessagesCount != 0) {
          TextLabel(
            text = "+${newMessagesCount}",
            modifier = Modifier.padding(
              end = LocalSpacing.current.medium,
              bottom = LocalSpacing.current.medium
            )
          )
        }
      }
    }
  )
}

@Composable
fun ProfileSettingsButton(
  profile: Profile,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(CircleShape)
      .background(Color.Black.copy(alpha = 0.3f))
      .clickable(onClick = onClick)
      .padding(LocalSpacing.current.extraSmall)
  ) {
    RemoteImage(
      model = profile.imageUrl,
      modifier = Modifier.size(LocalSize.current.medium)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    LikesLabel(count = profile.likesCount)

    Spacer(modifier = Modifier.width(LocalSpacing.current.medium))
  }
}

@Preview
@Composable
fun PreviewTopBar() {
  AppTheme {
    Surface {
      TopBar(
        profile = previewProfile,
        newMessagesCount = 1,
        onSettingsButtonClick = {  },
        onCloudButtonClick = {}
      )
    }
  }
}