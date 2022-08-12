package com.lofigroup.seeyau.features.auth_screen

import com.lofigroup.seeyau.features.auth_screen.model.AuthMode
import com.lofigroup.seeyau.features.auth_screen.model.AuthScreenState
import kotlinx.coroutines.flow.Flow

interface AuthScreenStateHolder {

  fun register(email: String, password: String, passwordDuplicate: String)

  fun login(email: String, password: String)

  fun getState(): Flow<AuthScreenState>

  fun changeAuthMode(mode: AuthMode)

}