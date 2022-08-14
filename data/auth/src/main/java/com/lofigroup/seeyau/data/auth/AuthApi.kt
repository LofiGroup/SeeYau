package com.lofigroup.seeyau.data.auth

import com.lofigroup.backend_api.models.AccessRequest
import com.lofigroup.backend_api.models.AccessResponse
import retrofit2.Response

interface AuthApi {

  suspend fun login(body: AccessRequest): Response<AccessResponse>

  suspend fun register(body: AccessRequest): Response<AccessResponse>

}