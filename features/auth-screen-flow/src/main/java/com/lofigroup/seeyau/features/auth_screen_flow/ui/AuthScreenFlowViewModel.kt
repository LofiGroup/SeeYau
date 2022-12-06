package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.lofigroup.seeyau.domain.auth.usecases.AuthorizeUseCase
import com.lofigroup.seeyau.domain.auth.usecases.StartAuthUseCase
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.lofigroup.seeyau.domain.profile.usecases.UpdateProfileUseCase
import com.lofigroup.seeyau.features.auth_screen_flow.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthScreenFlowViewModel @Inject constructor(
  private val authorizeUseCase: AuthorizeUseCase,
  private val startAuthUseCase: StartAuthUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel(), AuthScreenFlowStateHolder {

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
    state.value = state.value.copy(routePoint = routePoint)
  }

  override fun setImageUri(uri: Uri) {
    state.value = state.value.copy(
      imageUri = uri.toString()
    )
  }

  override fun updateProfile() {
    viewModelScope.launch {
      val result = updateProfileUseCase(ProfileUpdate(name = state.value.name, imageUrl = state.value.imageUri))

      if (result is Result.Success) {
        state.value = state.value.copy(allDataIsValid = true)
      }
    }
  }

  override fun throwError(errorMessage: String) {

  }

  override fun startAuth() {
    state.value = state.value.copy(
      enterNumberScreenState = EnterNumberScreenState.LOADING
    )
    viewModelScope.launch {
      val result = startAuthUseCase(state.value.toStartAuth())

      if (result is Resource.Success)
        state.value = state.value.copy(
          routePoint = RoutePoint.VerifyPhone,
          enterNumberScreenState = EnterNumberScreenState.TYPING,
          verifyCodeScreenState = VerifyCodeScreenState.TYPING
        )
      else {
        state.value = state.value.copy(
          enterNumberScreenState = EnterNumberScreenState.ERROR
        )
      }
    }
  }

  private fun verifyCode() {
    viewModelScope.launch {
      state.value = state.value.copy(
        verifyCodeScreenState = VerifyCodeScreenState.LOADING
      )
      val result = authorizeUseCase(state.value.toAccess())
      if (result is Resource.Success) {
        state.value = state.value.copy(
          verifyCodeScreenState = VerifyCodeScreenState.SUCCESS
        )
        delay(1000L)

        resolveNavigation(result.data)
      } else {
        state.value = state.value.copy(verifyCodeScreenState = VerifyCodeScreenState.ERROR)
      }
    }
  }

  private suspend fun resolveNavigation(authResponse: AuthResponse) {
    if (authResponse.exists) {
      state.value = state.value.copy(routePoint = RoutePoint.AlreadyRegistered)
      delay(2000L)
      state.value = state.value.copy(allDataIsValid = true)
    } else {
      state.value = state.value.copy(routePoint = RoutePoint.EnterName)
    }
  }

}