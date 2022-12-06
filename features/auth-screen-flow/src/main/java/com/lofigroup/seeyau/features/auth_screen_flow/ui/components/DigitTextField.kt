package com.lofigroup.seeyau.features.auth_screen_flow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.isDigitsOnly
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.model.VerifyCodeScreenState
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import timber.log.Timber

@Composable
fun DigitTextField(
  setCode: (String) -> Unit,
  state: VerifyCodeScreenState
) {
  val focusRequesters = remember {
    IntRange(start = 0, endInclusive = 3).map { FocusRequester() }
  }
  val data = remember {
    mutableStateListOf("", "", "", "")
  }
  val focusManager = LocalFocusManager.current

  Box() {
    Row(
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = Modifier
        .fillMaxWidth(),
    ) {
      for (i in 0 until 4)
        DigitItem(
          value = data[i],
          setValue = { value ->
            data[i] = value
            if (value.isEmpty()) {
              if (i > 0)
                focusRequesters[i - 1].requestFocus()
            } else if (i < 3)
              focusRequesters[i + 1].requestFocus()
            else {
              focusManager.clearFocus()
              setCode(data.joinToString(separator = "") { it })
            }
          },
          state = state,
          focusRequester = focusRequesters[i],
          goBack = {
            if (i == 0) return@DigitItem
            data[i - 1] = ""
            focusRequesters[i - 1].requestFocus()
          }
        )
    }
    Spacer(
      modifier = Modifier
        .height(LocalSize.current.bigMedium)
        .fillMaxWidth()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = {
            Timber.e("Clicked!")
            for (i in data.indices) data[i] = ""
            setCode("")
            focusManager.clearFocus()
            focusRequesters[0].requestFocus()
          }
        )

    )
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DigitItem(
  value: String,
  setValue: (String) -> Unit,
  goBack: () -> Unit,
  state: VerifyCodeScreenState,
  focusRequester: FocusRequester
) {
  val color =
    if (state == VerifyCodeScreenState.ERROR) MaterialTheme.colors.error else MaterialTheme.colors.primary

  Box(
    modifier = Modifier
      .border(
        width = 2.dp,
        color = color,
        shape = MaterialTheme.shapes.medium
      )
      .size(LocalSize.current.bigMedium)
  ) {
    OutlinedTextField(
      value = value,
      onValueChange = {
        if (it.isDigitsOnly())
          setValue(it.lastOrNull()?.toString() ?: "")
      },
      textStyle = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Center),
      colors = TextFieldDefaults.textFieldColors(
        textColor = color,
        disabledTextColor = LocalExtendedColors.current.disabled,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        backgroundColor = Color.Transparent
      ),
      enabled = state != VerifyCodeScreenState.LOADING,
      isError = state == VerifyCodeScreenState.ERROR,
      keyboardOptions = KeyboardOptions(
        autoCorrect = false,
        keyboardType = KeyboardType.Number
      ),
      modifier = Modifier
        .onKeyEvent {
          if (value.isEmpty() && it.key.keyCode == Key.Backspace.keyCode)
            goBack()
          true
        }
        .focusRequester(focusRequester)
        .align(Alignment.Center),
    )
  }
}

@Preview
@Composable
fun DigitTextFieldPreview() {
  AppTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      DigitTextField(setCode = { }, state = VerifyCodeScreenState.TYPING)
    }
  }
}