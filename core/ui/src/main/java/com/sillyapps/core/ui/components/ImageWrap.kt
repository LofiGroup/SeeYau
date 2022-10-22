package com.sillyapps.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun ImageWrap(
  resourceId: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
  ) {
    Image(
      painter = painterResource(id = resourceId),
      contentDescription = null,
      modifier = Modifier.align(Alignment.Center)
    )
  }
}