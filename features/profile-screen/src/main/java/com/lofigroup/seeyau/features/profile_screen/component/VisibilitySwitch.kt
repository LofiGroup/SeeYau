package com.lofigroup.seeyau.features.profile_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.R
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun VisibilitySwitch(
  visible: Boolean,
  onSetVisibilityState: (Boolean) -> Unit
) {
  
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = LocalSpacing.current.medium)
  ) {
    Image(
      painter = painterResource(id = CommonR.drawable.ic_bx_hide),
      contentDescription = null
    )
    
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    
    Text(
      text = stringResource(id = R.string.make_me_invisible),
      style = MaterialTheme.typography.h4,
      modifier = Modifier.weight(1f)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Switch(
      checked = !visible,
      onCheckedChange = { onSetVisibilityState(!it) }
    )

  }
  
}

@Preview
@Composable
fun PreviewVisibilitySwitch() {
  AppTheme {
    Surface {
      VisibilitySwitch(visible = true, onSetVisibilityState = {})
    }
  }
}