package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.lofigroup.seeyau.common.ui.components.LikesLabel
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ProfileSettingsButton(
  profile: Profile,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .padding(start = LocalSpacing.current.small)
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