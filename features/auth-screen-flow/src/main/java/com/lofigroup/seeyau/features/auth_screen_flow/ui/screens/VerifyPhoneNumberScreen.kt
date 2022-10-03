package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.VerifyCodeScreenState
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar
import com.sillyapps.core.ui.theme.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerifyPhoneNumberScreen(
  code: String,
  setCode: (String) -> Unit,
  phoneNumber: String,
  state: VerifyCodeScreenState
) {

  val textColor = if (state == VerifyCodeScreenState.ERROR)
    MaterialTheme.colors.error else MaterialTheme.colors.onBackground

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .padding(top = LocalSpacing.current.extraLarge, bottom = LocalSpacing.current.large)
  ) {
    Text(
      text = stringResource(id = R.string.verify_phone),
      style = MaterialTheme.typography.h2
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

    Text(
      text = stringResource(id = R.string.code_is_sent_to, phoneNumber),
      style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    TextField(
      value = code,
      onValueChange = setCode,
      textStyle = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Center),
      colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        textColor = textColor
      ),
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number
      ),
      enabled = state != VerifyCodeScreenState.LOADING,
      modifier = Modifier.onFocusChanged {
        if (it.isFocused) setCode("")
      }
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    when (state) {
      VerifyCodeScreenState.TYPING -> {}
      VerifyCodeScreenState.ERROR -> {
        LocalSoftwareKeyboardController.current?.hide()
        Text(
          text = stringResource(id = R.string.code_is_not_correct),
          style = MaterialTheme.typography.subtitle1,
          color = MaterialTheme.colors.error
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

}

@Preview
@Composable
fun VerifyPhoneNumberScreenPreview() {
  AppTheme() {
    Surface() {
      Column() {
        TopBar()

        VerifyPhoneNumberScreen(
          phoneNumber = "+79999999999",
          state = VerifyCodeScreenState.TYPING,
          code = "",
          setCode = {}
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
      Column() {
        TopBar()

        VerifyPhoneNumberScreen(
          code = "",
          setCode = {},
          phoneNumber = "+79999999999",
          state = VerifyCodeScreenState.ERROR
        )
      }
    }
  }
}