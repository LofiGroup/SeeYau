package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.model.*
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun AuthScreenRoot(
  stateHolder: AuthScreenFlowStateHolder,
  isDone: () -> Unit
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = AuthScreenFlowModel())

  LaunchedEffect(state.flowState) {
    if (state.flowState == AuthFlowState.ALL_DATA_IS_VALID) {
      isDone()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    when (state.routePoint) {
      RoutePoint.Welcome -> FirstScreen(
        onNextButtonClick = {
          stateHolder.setRoutePoint(RoutePoint.PickPicture)
        }
      )
      RoutePoint.EnterName -> {
        EnterNameScreen(
          isDone = {
            stateHolder.setName(it)
            stateHolder.setRoutePoint(RoutePoint.PickPicture)
          },
          name = state.name
        )
      }
      RoutePoint.EnterPhone -> {
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
        VerifyPhoneNumberScreen(
          setCode = stateHolder::setCode,
          phoneNumber = state.number,
          state = state.verifyCodeScreenState,
          onUpButtonClick = { stateHolder.setRoutePoint(RoutePoint.EnterPhone) }
        )
      }
      RoutePoint.PickPicture -> {
        AddPhotoScreen(
          imageUri = state.imageUri,
          setImageUri = stateHolder::setImageUri,
          throwError = stateHolder::throwError,
          update = stateHolder::quickAuth,
          flowState = state.flowState,
          onUpButtonClick = { stateHolder.setRoutePoint(RoutePoint.Welcome) }
        )
      }
      RoutePoint.AlreadyRegistered -> {
        AlreadyRegisteredScreen()
      }
    }
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

    override fun quickAuth() {
      scope.launch {
        delay(1000L)

        state.value = state.value.copy(flowState = AuthFlowState.ALL_DATA_IS_VALID)
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

