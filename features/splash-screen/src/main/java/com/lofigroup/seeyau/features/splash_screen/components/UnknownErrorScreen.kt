package com.lofigroup.seeyau.features.splash_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.ButtonWithText
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.splash_screen.R
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun UnknownErrorScreen(
  message: String,
  onTryAgain: () -> Unit
) {
  Box() {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = LocalSpacing.current.medium)
        .align(Alignment.Center)
    ) {
      Icon(
        imageVector = Icons.Filled.Error,
        contentDescription = null,
        modifier = Modifier.size(LocalSize.current.medium),
        tint = MaterialTheme.colors.error
      )
      Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

      Text(
        text = stringResource(id = R.string.unexpected_error),
        style = MaterialTheme.typography.h3,
      )
      Spacer(modifier = Modifier.height(LocalSpacing.current.small))

      Text(
        text = stringResource(id = R.string.unexpected_error_detail),
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

      Text(
        text = stringResource(id = R.string.error_message),
        style = MaterialTheme.typography.h4
      )
      Spacer(modifier = Modifier.height(LocalSpacing.current.small))

      Text(
        text = message,
        textAlign = TextAlign.Center
      )
    }

    ButtonWithText(
      text = stringResource(id = R.string.try_again),
      onClick = onTryAgain,
      modifier = Modifier
        .navigationBarsPadding()
        .fillMaxWidth()
        .padding(LocalSpacing.current.medium)
        .align(Alignment.BottomCenter)
    )
  }

}

@Preview
@Composable
fun UnknownErrorScreenPreview() {
  AppTheme {
    Surface {
      UnknownErrorScreen(message = "Some error", onTryAgain = {})
    }
  }
}