package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.EnterNumberScreenState
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar

@Composable
fun EnterPhoneNumberScreen(
  state: EnterNumberScreenState,
  phoneNumber: String,
  setPhoneNumber: (String) -> Unit,
  isDone: () -> Unit
) {

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .padding(top = LocalSpacing.current.extraLarge, bottom = LocalSpacing.current.large)
  ) {
    Text(
      text = stringResource(id = R.string.enter_your_phone_number),
      style = MaterialTheme.typography.h4
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
        keyboardType = KeyboardType.Number
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          isDone()
        }
      ),
      enabled = state != EnterNumberScreenState.LOADING
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    if (state == EnterNumberScreenState.ERROR) {
      Text(
        text = stringResource(id = R.string.something_went_wrong),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.error
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    when (state) {
      EnterNumberScreenState.TYPING -> {
        if (phoneNumber.isNotBlank()) {
          TextButton(
            onClick = { isDone() },
          ) {
            Text(
              text = stringResource(id = R.string.next),
              style = MaterialTheme.typography.h5
            )
          }
        }
      }
      EnterNumberScreenState.ERROR -> {

      }
      EnterNumberScreenState.LOADING -> {
        CircularProgressIndicator()
      }
    }


  }
}

@Preview
@Composable
fun EnterPhoneNumberScreenPreview() {
  AppTheme() {
    Surface() {
      Column() {
        TopBar()

        EnterPhoneNumberScreen(
          phoneNumber = "+79998437886",
          setPhoneNumber = {},
          isDone = {},
          state = EnterNumberScreenState.TYPING,
        )
      }
    }
  }
}