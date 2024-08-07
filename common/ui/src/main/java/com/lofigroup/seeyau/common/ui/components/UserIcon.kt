package com.lofigroup.seeyau.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lofigroup.seeyau.common.ui.R
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize

@Composable
fun UserIcon(
  imageUri: String?,
  isOnline: Boolean,
  onClick: () -> Unit = {}
) {
  Box() {
    RemoteImage(
      model = imageUri,
      onClick = onClick,
      modifier = Modifier
        .size(LocalSize.current.medium)
    )
  }
}