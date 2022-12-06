package com.lofigroup.seeyau.features.profile_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.features.profile_screen.R
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize

@Composable
fun UserIcon(
  imageUri: String,
  onClick: () -> Unit
) {
  val changeIconSize = DpSize(34.dp, 30.dp)
  
  Box {
    RemoteImage(
      model = imageUri,
      onClick = onClick,
      modifier = Modifier
        .padding(bottom = changeIconSize.width / 2)
        .size(LocalSize.current.veryLarge)
        .align(Alignment.TopCenter)
    )
    Image(
      painter = painterResource(id = R.drawable.ic_change_photo_icon),
      contentDescription = null,
      modifier = Modifier
        .size(changeIconSize)
        .align(Alignment.BottomCenter)
    )
  }
}