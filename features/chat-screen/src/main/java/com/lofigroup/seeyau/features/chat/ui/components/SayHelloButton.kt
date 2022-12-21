package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.features.chat.R
import com.sillyapps.core.ui.components.ImageButton
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun BoxScope.SayHelloButton(
  onSayHelloButtonClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.align(Alignment.Center)
  ) {
    Text(
      text = stringResource(id = R.string.say_hello),
      style = MaterialTheme.typography.h5
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    ImageButton(
      painter = painterResource(id = R.drawable.ic_hello),
      onClick = onSayHelloButtonClick
    )
  }
}