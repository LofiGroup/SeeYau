package com.lofigroup.seeyau.common.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R

@Composable
fun LikesLabel(
  count: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(id = R.drawable.ic_like_small),
      contentDescription = null
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = count.toString(),
      style = MaterialTheme.typography.h6
    )
  }
}

@Preview
@Composable
fun PreviewLikesLabel() {
  AppTheme {
    Surface() {
      LikesLabel(count = 423)
    }
  }
}