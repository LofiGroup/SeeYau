package com.lofigroup.seeyau.features.profile_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.lofigroup.seeyau.common.ui.R as CommonR
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun FaqScreen(
  onUpButtonClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .systemBarsPadding()
      .background(MaterialTheme.colors.surface)
  ) {
    DefaultTopBar(
      title = stringResource(id = R.string.faq),
      leftContent = { UpButton(onClick = onUpButtonClick) }
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

    for (faq in faqItems) {
      FaqDescription(faq)
    }
  }
  BackHandler(onBack = onUpButtonClick)
}

@Composable
fun FaqDescription(
  faqItem: FaqItem
) {
  Column() {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(LocalSize.current.medium)
          .padding(LocalSpacing.current.small)
      ) {
        Icon(
          painter = painterResource(id = faqItem.iconResId),
          contentDescription = null,
          modifier = Modifier
            .fillMaxSize()
        )
      }
      Text(
        text = stringResource(id = faqItem.titleResId),
        style = MaterialTheme.typography.h6
      )
    }

    Spacer(modifier = Modifier.height(LocalSpacing.current.small))

    Text(
      text = stringResource(id = faqItem.descriptionResId),
      textAlign = TextAlign.Center,
      color = LocalExtendedColors.current.lightBackground,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = LocalSpacing.current.small)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
  }
}

data class FaqItem(
  val iconResId: Int,
  val titleResId: Int,
  val descriptionResId: Int
)

private val faqItems = listOf(
  FaqItem(
    iconResId = CommonR.drawable.ploom_icon,
    titleResId = R.string.ploom_app,
    descriptionResId = R.string.ploom_app_description
  ),
  FaqItem(
    iconResId = CommonR.drawable.nearby_icon,
    titleResId = R.string.people_in_field_of_vision,
    descriptionResId = R.string.people_in_field_of_vision_description
  ),
  FaqItem(
    iconResId = CommonR.drawable.history,
    titleResId = R.string.people_you_met_today,
    descriptionResId = R.string.people_you_met_today_description
  ),
  FaqItem(
    iconResId = CommonR.drawable.ic_interaction,
    titleResId = R.string.chats,
    descriptionResId = R.string.chats_description
  ),
  FaqItem(
    iconResId = CommonR.drawable.ic_bx_hide,
    titleResId = R.string.make_me_invisible,
    descriptionResId = R.string.make_me_invisible_description
  ),
)

@Preview
@Composable
fun FaqScreenPreview() {
  AppTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      FaqScreen {

      }
    }
  }
}