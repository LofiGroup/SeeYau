package com.lofigroup.seeyau.domain.auth

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.StartAuth

interface AuthRepository {

  suspend fun logout()

  suspend fun authorize(access: Access): Resource<Unit>

  suspend fun startAuth(startAuth: StartAuth): Resource<Unit>

  suspend fun check(): Result

}