package com.lofigroup.seeyau.features.profile_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.domain.auth.usecases.LogoutUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetProfileUseCase
import com.lofigroup.seeyau.domain.profile.usecases.UpdateProfileUseCase
import com.lofigroup.seeyau.domain.settings.model.Visibility
import com.lofigroup.seeyau.domain.settings.usecases.GetVisibilityUseCase
import com.lofigroup.seeyau.domain.settings.usecases.SetVisibilityUseCase
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import com.lofigroup.seeyau.features.profile_screen.model.applyProfileUpdates
import com.lofigroup.seeyau.features.profile_screen.model.applyVisibilityUpdates
import com.lofigroup.seeyau.features.profile_screen.model.toProfileUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ProfileScreenViewModel @Inject constructor(
  private val getProfileUseCase: GetProfileUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase,
  private val getVisibilityUseCase: GetVisibilityUseCase,
  private val setVisibilityUseCase: SetVisibilityUseCase,
  private val logoutUseCase: LogoutUseCase
): ViewModel(), ProfileScreenStateHolder {

  private val state = MutableStateFlow(ProfileScreenState())

  init {
    observeProfileUpdates()
    observeVisibilityUpdates()
  }

  private fun observeVisibilityUpdates() {
    viewModelScope.launch {
      getVisibilityUseCase().collect() {
        state.value = state.value.applyVisibilityUpdates(it)
      }
    }
  }

  private fun observeProfileUpdates() {
    viewModelScope.launch {
      getProfileUseCase().collect() {
        state.value = state.value.applyProfileUpdates(it)
      }
    }
  }

  private fun validateData(): Boolean {
//    val data = state.value
//    if (data.name.isBlank()) {
//      state.value = data.copy(errorMessage = "Name is blank")
//      return false
//    }
    return true
  }

  private fun saveProfile() {
    val dataIsValid = validateData()
    if (!dataIsValid) return

    viewModelScope.launch {
      updateProfileUseCase(state.value.toProfileUpdate())
    }
  }

  override fun getState(): Flow<ProfileScreenState> = state

  override fun setName(name: String) {
    state.value = state.value.copy(name = name)
  }

  override fun throwError(errorMessage: String) {
    Timber.e("Error in profile screen: $errorMessage")
    state.value = state.value.copy(errorMessage = errorMessage)
  }

  override fun setImageUri(uri: Uri) {
    state.value = state.value.copy(imageUrl = uri.toString())
    saveProfile()
  }

  override fun setVisibility(isVisible: Boolean) {
    viewModelScope.launch {
      setVisibilityUseCase(Visibility(isVisible))
      state.apply { value = value.copy(isVisible = isVisible) }
    }
  }

  override fun deleteAccount() {
    viewModelScope.launch {
      logoutUseCase()
    }
  }

}