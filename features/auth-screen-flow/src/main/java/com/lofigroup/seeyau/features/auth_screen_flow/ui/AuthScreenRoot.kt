package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalIconsSize
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.AuthScreenFlowModel
import com.lofigroup.seeyau.features.auth_screen_flow.model.RoutePoint
import com.lofigroup.seeyau.features.auth_screen_flow.model.VerifyCodeScreenState
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.AddPhotoScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.EnterNameScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.EnterPhoneNumberScreen
import com.lofigroup.seeyau.features.auth_screen_flow.ui.screens.VerifyPhoneNumberScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AuthScreenRoot(
  stateHolder: AuthScreenFlowStateHolder,
  isDone: () -> Unit
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = AuthScreenFlowModel())

  Surface() {
    Column(
      modifier = Modifier
        .fillMaxSize()
    ) {
      TopBar()

      when (state.routePoint) {
        RoutePoint.EnterName -> {
          EnterNameScreen(
            isDone = {
              stateHolder.setName(it)
              stateHolder.setRoutePoint(RoutePoint.EnterPhone)
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
            code = state.code,
            setCode = stateHolder::setCode,
            phoneNumber = state.number,
            state = state.verifyCodeScreenState
          )
        }
        RoutePoint.PickPicture -> {
          AddPhotoScreen(
            imageUri = state.imageUri,
            setImageUri = stateHolder::setImageUri,
            throwError = stateHolder::throwError,
            dataIsValid = state.allDataIsValid,
            isDone = isDone
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
        imageVector = Icons.Filled.ArrowLeft,
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.CenterStart)
          .size(LocalIconsSize.current.medium),
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
  val stateHolder = object: AuthScreenFlowStateHolder {
    private val state = MutableStateFlow(AuthScreenFlowModel())

    override fun getState(): Flow<AuthScreenFlowModel> {
      return state
    }

    override fun setName(name: String) {
      state.value = state.value.copy(name = name)
    }

    override fun setNumber(number: String) {
      state.value = state.value.copy(number = number)
    }

    override fun setCode(code: String) {
      if (code.length <= 4) {
        state.value = state.value.copy(
          code = code,
          verifyCodeScreenState = VerifyCodeScreenState.TYPING
        )
      }
      if (code.length == 4) {
        verifyCode(code)
      }
    }

    override fun startAuth() {

    }

    override fun setRoutePoint(routePoint: RoutePoint) {
      state.value = state.value.copy(routePoint = routePoint)
    }

    private fun verifyCode(code: String) {
      if (code == "1234") {
        state.value = state.value.copy(
          verifyCodeScreenState = VerifyCodeScreenState.SUCCESS,
          routePoint = RoutePoint.PickPicture
        )
      }
      else {
        state.value = state.value.copy(verifyCodeScreenState = VerifyCodeScreenState.ERROR)
      }

    }

    override fun setImageUri(uri: Uri) {
      state.value = state.value.copy(
        imageUri = uri.toString(),
        allDataIsValid = true
      )
    }

    override fun throwError(errorMessage: String) {

    }

  }

  AppTheme {
    AuthScreenRoot(
      stateHolder = stateHolder,
      isDone = {}
    )
  }
}

