package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.lofigroup.seayau.common.ui.R
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UserIcon(
  imageUri: String?,
  isOnline: Boolean,
  onClick: () -> Unit
) {
  Box() {
    RemoteImage(
      model = imageUri,
      placeholderResId = R.drawable.ic_baseline_account_circle_24,
      errorPlaceholderResId = R.drawable.ic_baseline_account_circle_24,
      onClick = onClick,
      modifier = Modifier
        .size(LocalSize.current.large)
        .padding(LocalSpacing.current.small)
    )

    if (isOnline) {
      Image(
        painter = painterResource(
          id = R.drawable.ic_online_big
        ),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(end = LocalSpacing.current.medium)
      )
    }
  }
}