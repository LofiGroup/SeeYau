package com.lofigroup.seeyau.features.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BleNotSupportedScreen() {
  Box(modifier = Modifier.fillMaxSize()) {
    Text(
      text = "We're sorry, but your device doesn't support required bluetooth features.",
      textAlign = TextAlign.Center,
      modifier = Modifier
        .align(alignment = Alignment.Center)
        .padding(16.dp)
    )
  }
}