package com.lofigroup.seeyau.features.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.domain.auth.usecases.LoginUseCase
import com.lofigroup.seeyau.domain.auth.usecases.RegisterUseCase
import com.lofigroup.seeyau.features.auth_screen.model.AuthMode
import com.lofigroup.seeyau.features.auth_screen.model.AuthScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthScreenViewModel @Inject constructor(
  private val loginUseCase: LoginUseCase,
  private val registerUseCase: RegisterUseCase
): ViewModel(), AuthScreenStateHolder {

  private val state = MutableStateFlow(AuthScreenState())

  override fun register(email: String, password: String, passwordDuplicate: String) {
    if (!dataIsValid(email, password, passwordDuplicate))
      return

    viewModelScope.launch {
      val result = registerUseCase(Access(email, password))

      when (result) {
        is Resource.Success -> {
          state.value = state.value.copy(isSigned = true)
        }
        is Resource.Error -> {
          state.value = state.value.copy(error = result.errorMessage)
        }
        is Resource.Loading -> {

        }
      }
    }
  }

  override fun login(email: String, password: String) {
    if (!dataIsValid(email, password))
      return

    viewModelScope.launch {
      val result = loginUseCase(Access(email, password))

      when (result) {
        is Resource.Success -> {
          state.value = state.value.copy(isSigned = true)
        }
        is Resource.Error -> {
          state.value = state.value.copy(error = result.errorMessage)
        }
        is Resource.Loading -> {

        }
      }
    }
  }

  override fun getState(): Flow<AuthScreenState> = state

  override fun changeAuthMode(mode: AuthMode) {
    state.value = state.value.copy(authMode = mode)
  }

  private fun dataIsValid(email: String, password: String, passwordDuplicate: String? = null): Boolean {
    if (email.isBlank()) {
      state.value = state.value.copy(error = "Email shouldn't be blank")
      return false
    }
    if (password.isBlank()) {
      state.value = state.value.copy(error = "Password shouldn't be blank")
      return false
    }
    if (passwordDuplicate != null && password != passwordDuplicate) {
      state.value = state.value.copy(error = "Passwords don't match")
      return false
    }
    return true
  }


}