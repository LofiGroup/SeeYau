package com.lofigroup.seeyau.features.splash_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.features.splash_screen.R

@Composable
fun LoadingScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Text(
      text = stringResource(id = R.string.ploom),
      style = MaterialTheme.typography.h1,
      modifier = Modifier
        .align(Alignment.Center)
    )

    val annotatedText = buildAnnotatedString {
      withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
        append(stringResource(id = R.string.powered_by))
      }
      withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
        append(" ${stringResource(id = R.string.bluetooth)}")
      }
    }
    Text(
      text = annotatedText,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = LocalSpacing.current.extraLarge)
        .navigationBarsPadding()
    )
  }
}
