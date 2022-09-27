package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun EnterNameScreen(
  name: String,
  isDone: (String) -> Unit
) {
  val (text, setText) = rememberSaveable {
    mutableStateOf(name)
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .padding(top = LocalSpacing.current.extraLarge, bottom = LocalSpacing.current.large)
  ) {
    Text(
      text = stringResource(id = R.string.whats_your_name),
      style = MaterialTheme.typography.h4
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    TextField(
      value = text,
      onValueChange = setText,
      textStyle = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
      colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent
      ),
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          isDone(text)
        }
      )
    )

    Spacer(modifier = Modifier.weight(1f))

    if (text.length > 1) {
      TextButton(
        onClick = { isDone(text) },
      ) {
        Text(
          text = stringResource(id = R.string.next),
          style = MaterialTheme.typography.h5
        )
      }
    }
  }
}

@Preview
@Composable
fun EnterNameScreenPreview() {
  AppTheme() {
    Surface() {
      Column() {
        TopBar()

        EnterNameScreen(
          name = "Jacov",
          isDone = {}
        )
      }
    }
  }
}