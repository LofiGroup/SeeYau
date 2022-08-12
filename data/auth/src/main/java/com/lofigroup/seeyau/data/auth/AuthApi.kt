package com.lofigroup.seeyau.data.auth

import com.lofigroup.seeyau.data.auth.model.AccessRequest
import com.lofigroup.seeyau.data.auth.model.AccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

  @POST("/login/")
  suspend fun login(@Body body: AccessRequest): Response<AccessResponse>

  @POST("/register/")
  suspend fun register(@Body body: AccessRequest): Response<AccessResponse>

}