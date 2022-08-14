package com.lofigroup.seeyau.features.profile_screen

import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import kotlinx.coroutines.flow.Flow

interface ProfileScreenStateHolder {

  fun getState(): Flow<ProfileScreenState>

  fun setName(name: String)

  fun saveProfile()

}