package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.getLocalizedLastSeen
import com.lofigroup.seayau.common.ui.theme.LocalIconsSize
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun TopBar(
  partner: User,
  onUpButtonClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background.copy(alpha = 0.5f))
      .padding(LocalSpacing.current.medium)
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_arrow_1_icon),
      contentDescription = null,
      modifier = Modifier.clickable { onUpButtonClick() }
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    UserIcon(
      imageUri = partner.imageUrl,
      isOnline = partner.isOnline,
      onClick = {  }
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Column(
      modifier = Modifier
        .weight(1f)
    ) {
      Text(
        text = partner.name,
        style = MaterialTheme.typography.body2
      )
      Text(
        text = getLocalizedLastSeen(partner.lastConnection, LocalContext.current.resources),
        style = MaterialTheme.typography.subtitle2
      )
    }

    Image(
      painter = painterResource(id = R.drawable.ic_more_1_icon),
      contentDescription = null
    )
  }
}

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
        .size(LocalSize.current.medium)
    )

    if (isOnline) {
      Image(
        painter = painterResource(
          id = R.drawable.ic_online_small
        ),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.BottomEnd)
      )
    }
  }
}