package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalIconsSize
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.lofigroup.seayau.common.ui.theme.applyActivityBarPaddings
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.*
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.AddPhotoScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.EnterNameScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.EnterPhoneNumberScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.VerifyPhoneNumberScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun AuthScreenRoot(
  stateHolder: AuthScreenFlowStateHolder,
  isDone: () -> Unit
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = AuthScreenFlowModel())

  LaunchedEffect(state) {
    if (state.allDataIsValid) {
      isDone()
    }
  }

  Surface(
    modifier = Modifier.applyActivityBarPaddings()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
    ) {
      when (state.routePoint) {
        RoutePoint.EnterName -> {
          TopBar()
          EnterNameScreen(
            isDone = {
              stateHolder.setName(it)
              stateHolder.setRoutePoint(RoutePoint.EnterPhone)
            },
            name = state.name
          )
        }
        RoutePoint.EnterPhone -> {
          TopBar()
          EnterPhoneNumberScreen(
            isDone = {
              stateHolder.startAuth()
            },
            phoneNumber = state.number,
            setPhoneNumber = stateHolder::setNumber,
            state = state.enterNumberScreenState
          )
        }

        RoutePoint.VerifyPhone -> {
          TopBar()
          VerifyPhoneNumberScreen(
            code = state.code,
            setCode = stateHolder::setCode,
            phoneNumber = state.number,
            state = state.verifyCodeScreenState
          )
        }
        RoutePoint.PickPicture -> {
          AddPhotoScreen(
            topBar = { TopBar() },
            imageUri = state.imageUri,
            setImageUri = stateHolder::setImageUri,
            throwError = stateHolder::throwError,
            update = stateHolder::updateProfile
          )
        }
      }
    }
  }

}

@Composable
fun TopBar() {
  Box(
    Modifier
      .fillMaxWidth()
      .padding(top = LocalSpacing.current.medium)
  ) {
    IconButton(
      onClick = {}
    ) {
      Icon(
        painter = painterResource(id = CommonR.drawable.ic_arrow_1_icon),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.CenterStart),
      )
    }
    Text(
      text = stringResource(id = R.string.authorization),
      style = MaterialTheme.typography.h5,
      modifier = Modifier.align(Alignment.Center)
    )
  }
}

@Preview
@Composable
fun AuthScreenRootPreview() {
  val scope = rememberCoroutineScope()

  val stateHolder = object: AuthScreenFlowStateHolder {
    private val state = MutableStateFlow(AuthScreenFlowModel())

    override fun getState(): Flow<AuthScreenFlowModel> {
      return state
    }

    override fun setName(name: String) {
      state.value = state.value.copy(name = name)
    }

    override fun setNumber(number: String) {
      state.value = state.value.copy(
        number = number,
        enterNumberScreenState = EnterNumberScreenState.TYPING
      )
    }

    override fun setCode(code: String) {
      if (code.length <= 4) {
        state.value = state.value.copy(
          code = code,
          verifyCodeScreenState = VerifyCodeScreenState.TYPING
        )
      }
      if (code.length == 4) {
        verifyCode()
      }
    }

    override fun setRoutePoint(routePoint: RoutePoint) {
      if (routePoint == RoutePoint.VerifyPhone) {
        startAuth()
      } else state.value = state.value.copy(routePoint = routePoint)
    }

    override fun setImageUri(uri: Uri) {
      state.value = state.value.copy(
        imageUri = uri.toString()
      )
    }

    override fun updateProfile() {
      scope.launch {
        delay(1000L)

        state.value = state.value.copy(allDataIsValid = true)
      }
    }

    override fun throwError(errorMessage: String) {

    }

    override fun startAuth() {
      state.value = state.value.copy(
        enterNumberScreenState = EnterNumberScreenState.LOADING
      )
      scope.launch {
        delay(1000L)

        state.value = state.value.copy(
          routePoint = RoutePoint.VerifyPhone
        )
      }
    }

    private fun verifyCode() {
      scope.launch {
        state.value = state.value.copy(
          verifyCodeScreenState = VerifyCodeScreenState.LOADING
        )
        delay(1000L)
        if (state.value.code == "1234") {
          state.value = state.value.copy(
            verifyCodeScreenState = VerifyCodeScreenState.SUCCESS
          )
          delay(1000L)
          state.value = state.value.copy(
            routePoint = RoutePoint.PickPicture
          )
        } else {
          state.value = state.value.copy(verifyCodeScreenState = VerifyCodeScreenState.ERROR)
        }
      }
    }

  }

  AppTheme {
    AuthScreenRoot(
      stateHolder = stateHolder,
      isDone = {}
    )
  }
}

