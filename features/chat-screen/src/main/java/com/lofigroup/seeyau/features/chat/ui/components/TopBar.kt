package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.lofigroup.seeyau.common.ui.components.UserIcon
import com.lofigroup.seeyau.common.ui.getLocalizedLastSeen
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core.ui.components.ImageButton
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R

@Composable
fun TopBar(
  partner: User,
  onUpButtonClick: () -> Unit,
  onUserIconClick: () -> Unit
) {
  val context = LocalContext.current
  val lastSeen = remember(partner.lastConnection) { getLocalizedLastSeen(partner.lastConnection, context.resources) }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background.copy(alpha = 0.5f))
      .padding(vertical = LocalSpacing.current.medium)
  ) {
    ImageButton(
      onClick = onUpButtonClick,
      painter = painterResource(id = R.drawable.ic_arrow_1_icon)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    UserIcon(
      imageUri = partner.imageUrl,
      isOnline = partner.isOnline,
      onClick = onUserIconClick
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = lastSeen,
      style = MaterialTheme.typography.subtitle2
    )
  }
}