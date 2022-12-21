package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lofigroup.features.navigator_screen.R
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun NotVisibleScreen() {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    Text(
      text = stringResource(id = R.string.unvisible_details),
      style = MaterialTheme.typography.body1,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(horizontal = LocalSpacing.current.medium)
        .padding(bottom = LocalSpacing.current.medium)
    )
    Image(
      painter = painterResource(id = CommonR.drawable.ic_bx_hide),
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.medium)
    )
  }
}