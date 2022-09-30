package com.lofigroup.seayau.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun DefaultTopBar(
  title: String,
  leftContent: @Composable RowScope.() -> Unit = {},
  rightContent: @Composable RowScope.() -> Unit = {}
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(LocalSpacing.current.bigMedium)
  ) {
    Row(
      modifier = Modifier.align(Alignment.CenterStart)
    ) {
      leftContent()
    }

    Text(
      text = title,
      style = MaterialTheme.typography.h3,
      modifier = Modifier.align(Alignment.Center)
    )

    Row(
      modifier = Modifier.align(Alignment.CenterEnd)
    ) {
      rightContent()
    }
  }
}

@Composable
fun UpButton(
  onClick: () -> Unit
) {
  Image(
    painter = painterResource(id = R.drawable.ic_arrow_1_icon),
    contentDescription = null,
    modifier = Modifier
      .clickable { onClick() }
  )
}

@Preview
@Composable
fun PreviewTopBar() {
  AppTheme {
    Surface() {
      DefaultTopBar(
        title = "Main screen",
        leftContent = {
          UpButton {

          }
        },
        rightContent = {
          Image(
            painter = painterResource(id = R.drawable.ic_more_1_icon),
            contentDescription = null,
            modifier = Modifier
              .clickable { }
          )
        }
      )
    }
  }
}