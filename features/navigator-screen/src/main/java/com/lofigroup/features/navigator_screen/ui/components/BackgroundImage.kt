package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.LocalExtendedColors
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.sillyapps.core.ui.components.RemoteImage

@Composable
fun BackgroundImage(
  selectedUser: UserItemUIModel?
) {
  if (selectedUser != null) {
    RemoteImage(
      model = selectedUser.imageUrl,
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