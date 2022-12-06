package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.ButtonWithText
import com.lofigroup.seeyau.common.ui.components.FullScreenImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.EnterNumberScreenState
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.Description
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.TopBar
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun BoxScope.EnterPhoneNumberScreen(
  state: EnterNumberScreenState,
  phoneNumber: String,
  setPhoneNumber: (String) -> Unit,
  isDone: () -> Unit
) {
  val focusManager = LocalFocusManager.current

  FullScreenImage(painter = painterResource(id = R.drawable.phone_number_background))

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
  ) {
    TopBar()
    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    Description(
      title = stringResource(id = R.string.enter_your_phone_number),
      caption = stringResource(id = R.string.account_will_link_to)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    TextField(
      value = phoneNumber,
      onValueChange = setPhoneNumber,
      textStyle = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
      colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent
      ),
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Phone
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          isDone()
          focusManager.clearFocus()
        }
      ),
      enabled = state != EnterNumberScreenState.LOADING
    )

    if (state == EnterNumberScreenState.LOADING) {
      CircularProgressIndicator()
    }
  }

  ButtonWithText(
    text = stringResource(id = R.string.next),
    onClick = isDone,
    enabled = phoneNumber.length > 5 && state == EnterNumberScreenState.TYPING,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium, vertical = LocalSpacing.current.large)
      .navigationBarsPadding()
      .imePadding()
      .align(Alignment.BottomCenter)
  )
}

@Preview()
@Composable
fun EnterPhoneNumberScreenPreview() {
  var phoneNumber by remember {
    mutableStateOf("+79998437886")
  }

  AppTheme() {
    Surface() {
      Box() {
        EnterPhoneNumberScreen(
          phoneNumber = phoneNumber,
          setPhoneNumber = { phoneNumber = it },
          isDone = {},
          state = EnterNumberScreenState.TYPING,
        )
      }
    }
  }
}