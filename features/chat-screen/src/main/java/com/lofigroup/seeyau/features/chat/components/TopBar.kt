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
import com.lofigroup.seayau.common.ui.components.DefaultTopBar
import com.lofigroup.seayau.common.ui.theme.LocalIconsSize
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat.getLastSeen
import com.sillyapps.core.ui.components.RemoteImage
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

    RemoteImage(
      model = partner.imageUrl,
      placeholderResId = R.drawable.ic_baseline_account_box_24,
      errorPlaceholderResId = R.drawable.ic_baseline_account_box_24,
      modifier = Modifier
        .size(LocalIconsSize.current.medium)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))

    Column(
      modifier = Modifier
        .weight(1f)
    ) {
      Text(
        text = partner.name,
        style = MaterialTheme.typography.body2
      )
      Text(
        text = getLastSeen(
          millis = partner.lastConnection,
          context = LocalContext.current
        ),
        style = MaterialTheme.typography.subtitle2
      )
    }

    Image(
      painter = painterResource(id = R.drawable.ic_more_1_icon),
      contentDescription = null
    )
  }
}