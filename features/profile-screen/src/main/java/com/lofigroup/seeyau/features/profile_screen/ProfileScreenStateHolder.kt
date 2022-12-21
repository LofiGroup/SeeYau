package com.lofigroup.seeyau.features.profile_screen

import android.net.Uri
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import kotlinx.coroutines.flow.Flow

interface ProfileScreenStateHolder {

  fun getState(): Flow<ProfileScreenState>

  fun setName(name: String)

  fun throwError(errorMessage: String)

  fun setImageUri(uri: Uri)

  fun setVisibility(isVisible: Boolean)

  fun deleteAccount()

}