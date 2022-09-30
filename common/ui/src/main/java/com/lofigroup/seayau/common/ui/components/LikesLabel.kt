package com.lofigroup.seayau.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.theme.AppTheme

@Composable
fun LikesLabel(
  count: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_like_small),
      contentDescription = null
    )

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