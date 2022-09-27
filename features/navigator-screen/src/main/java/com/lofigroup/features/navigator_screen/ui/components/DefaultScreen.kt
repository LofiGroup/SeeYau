package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lofigroup.features.navigator_screen.R
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ColumnScope.DefaultScreen() {
  Box(
    modifier = Modifier
      .weight(1f)
      .padding(bottom = LocalSpacing.current.medium)
  ) {
    Text(
      text = stringResource(id = R.string.perhaps_someone_is_near),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.body1,
      modifier = Modifier
        .padding(horizontal = LocalSpacing.current.large)
        .align(Alignment.BottomCenter)
    )
  }
}

