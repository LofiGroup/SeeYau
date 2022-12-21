package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun BoxScope.EnterNameScreen(
  name: String,
  isDone: (String) -> Unit
) {
  val (text, setText) = rememberSaveable {
    mutableStateOf(name)
  }

  val focusRequester = remember { FocusRequester() }

  FullScreenImage(painter = painterResource(id = R.drawable.name_background))

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
  ) {
    TopBar()
    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    Description(
      title = stringResource(id = R.string.whats_your_name),
      caption = stringResource(id = R.string.how_can_people_address_you)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    TextField(
      value = text,
      onValueChange = setText,
      textStyle = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
      colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          isDone(text)
        }
      ),
      modifier = Modifier.focusRequester(focusRequester)
    )
  }

  ButtonWithText(
    text = stringResource(id = R.string.next),
    onClick = { isDone(text) },
    enabled = text.length > 1,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium, vertical = LocalSpacing.current.large)
      .navigationBarsPadding()
      .imePadding()
      .align(Alignment.BottomCenter)
  )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}

@Preview
@Composable
fun EnterNameScreenPreview() {
  AppTheme() {
    Surface() {
      Box() {
        EnterNameScreen(
          name = "Jacov",
          isDone = {}
        )
      }
    }
  }
}