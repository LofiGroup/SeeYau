package com.lofigroup.seeyau.features.profile_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun SettingButton(
  onClick: () -> Unit,
  iconResId: Int,
  label: String
) {

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium)
      .clip(MaterialTheme.shapes.medium)
      .background(LocalExtendedColors.current.primaryGradient)
      .fillMaxWidth()
      .clickable { onClick() }
  ) {

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Image(
      painter = painterResource(id = iconResId),
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.small)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = label,
      style = MaterialTheme.typography.h4,
      modifier = Modifier.weight(1f)
    )
    
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    
    Image(
      painter = painterResource(id = R.drawable.ic_arrow_setting),
      contentDescription = null
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

  }

}

@Preview
@Composable
fun PreviewSettingButton() {

  AppTheme() {
    Surface() {
      SettingButton(
        onClick = { /*TODO*/ },
        iconResId = R.drawable.ic_fluent_person_edit_24_filled,
        label = "Account"
      )
    }
  }

}