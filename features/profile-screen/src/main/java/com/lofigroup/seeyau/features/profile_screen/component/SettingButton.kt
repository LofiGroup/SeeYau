package com.lofigroup.seeyau.features.profile_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.R
import com.lofigroup.seeyau.common.ui.R as CommonR
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun SettingButton(
  iconResId: Int,
  label: String,
  onClick: () -> Unit,
  trailingContent: @Composable () -> Unit = {}
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium, vertical = LocalSpacing.current.small)
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

    trailingContent()

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

  }

}

@Composable
fun SettingFolder(
  iconResId: Int,
  label: String,
  content: @Composable ColumnScope.() -> Unit
) {
  var expanded by rememberSaveable() {
    mutableStateOf(false)
  }

  Column() {
    SettingButton(
      iconResId = iconResId,
      label = label,
      onClick = { expanded = !expanded },
      trailingContent = {
        Icon(
          painter = painterResource(id = if (!expanded) CommonR.drawable.ic_arrow_up else CommonR.drawable.ic_arrow_down),
          contentDescription = null
        )
      }
    )

    if (expanded) {
      Column(
        modifier = Modifier.padding(
          horizontal = LocalSpacing.current.medium,
          vertical = LocalSpacing.current.small
        )
      ) {
        content()
      }
    }
  }
}

@Preview
@Composable
fun PreviewSettingButton() {

  AppTheme() {
    Surface() {
      SettingButton(
        onClick = {  },
        iconResId = R.drawable.ic_account_edit,
        label = "Account"
      )
    }
  }

}