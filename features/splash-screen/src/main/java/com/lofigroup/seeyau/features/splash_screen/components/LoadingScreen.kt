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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.features.splash_screen.R

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoadingScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_intro_logo),
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.Center)
        .size(64.dp)
    )

    Text(
      text = stringResource(id = R.string.plum),
      style = MaterialTheme.typography.h1.copy(
        fontFamily = FontFamily(Font(R.font.swis721blkbtrusbyme_black)),
        brush = LocalExtendedColors.current.primaryGradient
      ),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding()
        .padding(bottom = LocalSpacing.current.extraLarge)
    )
  }
}
