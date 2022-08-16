package com.lofigroup.seeyau.features.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.lofigroup.seeyau.domain.profile.usecases.GetProfileUseCase
import com.lofigroup.seeyau.domain.profile.usecases.UpdateProfileUseCase
import com.lofigroup.seeyau.features.profile_screen.di.ProfileScreenComponent
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import com.lofigroup.seeyau.features.profile_screen.model.toProfile
import com.lofigroup.seeyau.features.profile_screen.model.toProfileScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileScreenViewModel @Inject constructor(
  private val getProfileUseCase: GetProfileUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase
): ViewModel(), ProfileScreenStateHolder {

  private val state = MutableStateFlow(ProfileScreenState())

  init {
    viewModelScope.launch {
      getProfileUseCase().collect() {
        state.value = when (it) {
          is Resource.Success -> it.data.toProfileScreenState()
          is Resource.Error -> ProfileScreenState(errorMessage = it.errorMessage, isLoading = false)
          is Resource.Loading -> ProfileScreenState(isLoading = true)
        }
      }
    }
  }

  private fun validateData(): Boolean {
    val data = state.value
    if (data.name.isBlank()) {
      state.value = data.copy(errorMessage = "Name is blank")
      return false
    }
    return true
  }

  override fun getState(): Flow<ProfileScreenState> = state

  override fun setName(name: String) {
    state.value = state.value.copy(name = name)
  }

  override fun saveProfile() {
    val dataIsValid = validateData()
    if (!dataIsValid) return

    state.value = state.value.copy(isLoading = true)
    viewModelScope.launch {
      state.value = when (val result = updateProfileUseCase(state.value.toProfile())) {
        is Result.Success -> state.value.copy(isLoading = false, navigateOut = true)
        is Result.Error -> state.value.copy(isLoading = false, errorMessage = result.message)
        is Result.Undefined -> state.value.copy(isLoading = false, errorMessage = result.message)
      }
    }
  }

}