package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.FullScreenImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.VerifyCodeScreenState
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.Description
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.DigitTextField
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.TopBar
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxScope.VerifyPhoneNumberScreen(
  setCode: (String) -> Unit,
  phoneNumber: String,
  state: VerifyCodeScreenState,
  onUpButtonClick: () -> Unit
) {
  FullScreenImage(
    painter = if (state == VerifyCodeScreenState.ERROR)
      painterResource(id = R.drawable.wrong_code_background)
    else
      painterResource(id = R.drawable.verification_background),
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
  ) {
    TopBar(onUpButtonClick = onUpButtonClick)
    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    Description(
      title = stringResource(id = R.string.verify_phone),
      caption = stringResource(id = R.string.code_is_sent_to, phoneNumber)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    DigitTextField(setCode = setCode, state = state)

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    when (state) {
      VerifyCodeScreenState.TYPING -> {}
      VerifyCodeScreenState.ERROR -> {
        Text(
          text = stringResource(id = R.string.code_is_not_correct),
          style = MaterialTheme.typography.subtitle1,
          color = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        Text(
          text = stringResource(id = R.string.resend_code),
          style = MaterialTheme.typography.subtitle2.copy(color = LocalExtendedColors.current.disabled)
        )
      }
      VerifyCodeScreenState.SUCCESS -> {
        LocalSoftwareKeyboardController.current?.hide()
        Text(
          text = stringResource(id = R.string.code_is_correct),
          style = MaterialTheme.typography.subtitle1,
          color = MaterialTheme.colors.secondary
        )
      }
      VerifyCodeScreenState.LOADING -> {
        CircularProgressIndicator()
      }
    }
  }

  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(Unit) {
    keyboardController?.hide()
  }
}

@Preview
@Composable
fun VerifyPhoneNumberScreenPreview() {
  AppTheme() {
    Surface() {
      Box() {
        VerifyPhoneNumberScreen(
          phoneNumber = "+79999999999",
          state = VerifyCodeScreenState.TYPING,
          setCode = {},
          onUpButtonClick = {}
        )
      }
    }
  }
}

@Preview
@Composable
fun VerifyPhoneNumberCodeIsWrongScreenPreview() {
  AppTheme() {
    Surface() {
      Box() {
        VerifyPhoneNumberScreen(
          setCode = {},
          phoneNumber = "+79999999999",
          state = VerifyCodeScreenState.ERROR,
          onUpButtonClick = {}
        )
      }
    }
  }
}