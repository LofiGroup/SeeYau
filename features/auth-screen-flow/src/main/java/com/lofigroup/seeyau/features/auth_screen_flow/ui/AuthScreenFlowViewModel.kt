package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.ResourceState
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.lofigroup.seeyau.domain.auth.usecases.AuthorizeUseCase
import com.lofigroup.seeyau.domain.auth.usecases.QuickAuthUseCase
import com.lofigroup.seeyau.domain.auth.usecases.StartAuthUseCase
import com.lofigroup.seeyau.features.auth_screen_flow.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AuthScreenFlowViewModel @Inject constructor(
  private val authorizeUseCase: AuthorizeUseCase,
  private val startAuthUseCase: StartAuthUseCase,
  private val quickAuthUseCase: QuickAuthUseCase,
  private val moduleStateHolder: ResourceStateHolder
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

  override fun quickAuth() {
    viewModelScope.launch {
      state.value = state.value.copy(flowState = AuthFlowState.SYNCING_DATA)
      val result = quickAuthUseCase(state.value.imageUri)

      when (result) {
        is Resource.Success -> {
          observeDataSyncState()
        }
        else -> {
          state.value = state.value.copy(flowState = AuthFlowState.ERROR)
        }
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

  private suspend fun observeDataSyncState() {
    moduleStateHolder.observe().collect() {
      Timber.e("Data sync state is $it")
      when (it) {
        ResourceState.LOADING, ResourceState.INITIALIZED -> Unit
        ResourceState.IS_READY -> {
          state.value = state.value.copy(flowState = AuthFlowState.ALL_DATA_IS_VALID)
        }
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
      state.value = state.value.copy(flowState = AuthFlowState.ALL_DATA_IS_VALID)
    } else {
      state.value = state.value.copy(routePoint = RoutePoint.EnterName)
    }
  }

}