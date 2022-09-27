package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors

@Composable
fun BackgroundImage(
  selectedUser: UserItemUIModel?,
  isInFullScreenMode: Boolean
) {
  if (selectedUser != null && isInFullScreenMode) {
    RemoteImage(
      model = selectedUser.imageUrl,
      shape = RectangleShape,
      modifier = Modifier
        .fillMaxSize()
    )
  } else {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(LocalExtendedColors.current.backgroundGradient)
    ) {

    }
  }
}