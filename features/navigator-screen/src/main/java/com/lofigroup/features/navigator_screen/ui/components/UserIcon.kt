package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.ui.R
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UserIcon(
  imageUri: String?,
  isOnline: Boolean,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
  ) {
    RemoteImage(
      model = imageUri,
      onClick = onClick,
      modifier = Modifier
        .size(LocalSize.current.large)
    )

    if (isSelected) {
      Box(modifier = Modifier
        .size(LocalSize.current.large)
        .border(width = 4.dp, brush = LocalExtendedColors.current.secondaryVerticalGradient, shape = CircleShape)
      ) {
      }
    }

    if (isOnline) {
      Image(
        painter = painterResource(
          id = R.drawable.ic_online_big
        ),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(end = LocalSpacing.current.small)
      )
    }
  }
}