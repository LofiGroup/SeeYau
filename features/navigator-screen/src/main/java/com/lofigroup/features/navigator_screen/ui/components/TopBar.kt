package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.seayau.common.ui.components.DefaultTopBar
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  newMessagesCount: Int,
  onSettingsButtonClick: () -> Unit,
  onCloudButtonClick: () -> Unit
) {
  DefaultTopBar(
    title = "",
    leftContent = {
      Image(
        painter = painterResource(id = R.drawable.ic_setting_icon),
        contentDescription = null,
        modifier = Modifier
          .clickable { onSettingsButtonClick() }
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
