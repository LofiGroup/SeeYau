package com.lofigroup.seeyau.domain.auth

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.lofigroup.seeyau.domain.auth.model.LoggedInStatus
import com.lofigroup.seeyau.domain.auth.model.StartAuth

interface AuthRepository {

  suspend fun logout()

  suspend fun authorize(access: Access): Resource<AuthResponse>

  suspend fun startAuth(startAuth: StartAuth): Resource<Unit>

  suspend fun check(): LoggedInStatus

  suspend fun quickAuth(imageUri: String): Resource<Unit>

  suspend fun sendFirebaseToken(token: String?)

}