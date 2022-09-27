package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.lofigroup.features.navigator_screen.R
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  newMessagesCount: Int,
  onSettingsButtonClick: () -> Unit,
  onCloudButtonClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(LocalSpacing.current.bigMedium)
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_setting_icon),
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .clickable { onSettingsButtonClick() }
    )
    Box(
      modifier = Modifier.align(Alignment.CenterEnd)
    ) {
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
}
