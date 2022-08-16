package com.lofigroup.seeyau.domain.auth

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.Token

interface AuthRepository {

  suspend fun login(access: Access): Resource<Unit>

  suspend fun logout()

  suspend fun register(access: Access): Resource<Unit>

  suspend fun check(): Result

}