package com.lofigroup.seeyau.features.auth_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen.model.AuthMode
import com.lofigroup.seeyau.features.auth_screen.model.AuthScreenState
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

@Composable
fun AuthScreen(
  stateHolder: AuthScreenStateHolder,
  onSuccessfulSign: (Boolean) -> Unit,
  ) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = AuthScreenState())

  if (state.isSigned) {
    LaunchedEffect(state) {
      onSuccessfulSign(state.authMode == AuthMode.SIGN_UP)
    }
  }

  val (email, setEmail) = rememberSaveable {
    mutableStateOf("")
  }
  val (password, setPassword) = rememberSaveable {
    mutableStateOf("")
  }
  val (passwordDuplicate, setPasswordDuplicate) = rememberSaveable {
    mutableStateOf("")
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.fillMaxSize()
  ) {
    TextField(
      value = email,
      onValueChange = setEmail,
      label = { Text(text = "Email") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    )

    TextField(
      value = password,
      onValueChange = setPassword,
      label = { Text(text = "Password") },
      modifier = Modifier
        .padding(vertical = 16.dp)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    )

    when (state.authMode) {
      AuthMode.SIGN_UP -> {
        TextField(
          value = passwordDuplicate,
          onValueChange = setPasswordDuplicate,
          label = { Text(text = "Repeat password") },
          modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        )
        Button(onClick = {
          stateHolder.register(
            email = email, password = password, passwordDuplicate = passwordDuplicate
          )
        }) {
          Text(text = "Sign up")
        }

        TextButton(
          onClick = { stateHolder.changeAuthMode(AuthMode.SIGN_IN) },
          modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
          Text(text = "Have a account?")
        }
      }

      AuthMode.SIGN_IN -> {
        Button(onClick = { stateHolder.login(email = email, password = password) }) {
          Text(text = "Sign in")
        }

        TextButton(
          onClick = { stateHolder.changeAuthMode(AuthMode.SIGN_UP) },
          modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
          Text(text = "Not registered?")
        }
      }
    }
  }

  val errorMessage = state.error
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }
}

@Preview
@Composable
fun AuthScreenPreview() {
  val stateHolder = object : AuthScreenStateHolder {
    val state = MutableStateFlow(AuthScreenState())

    override fun register(email: String, password: String, passwordDuplicate: String) {

    }

    override fun login(email: String, password: String) {

    }

    override fun getState(): Flow<AuthScreenState> = state

    override fun changeAuthMode(mode: AuthMode) {
      state.value = state.value.copy(authMode = mode)
    }

  }

  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      AuthScreen(
        stateHolder = stateHolder,
        onSuccessfulSign = {}
      )
    }
  }
}