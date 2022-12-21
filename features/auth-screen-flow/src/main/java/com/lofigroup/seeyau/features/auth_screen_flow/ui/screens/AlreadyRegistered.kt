package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.lofigroup.seeyau.common.ui.components.FullScreenImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.TopBar
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun AlreadyRegisteredScreen() {
  FullScreenImage(painter = painterResource(id = R.drawable.name_background))
  
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
  ) {
    Text(
      text = stringResource(id = R.string.already_registered),
      style = MaterialTheme.typography.h2,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = LocalSpacing.current.medium)
    )
  }
}

@Preview
@Composable
fun AlreadyRegisteredScreenPreview() {
  AppTheme {
    Surface() {
      AlreadyRegisteredScreen()
    }
  }
}