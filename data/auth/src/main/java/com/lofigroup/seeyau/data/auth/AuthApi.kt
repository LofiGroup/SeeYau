package com.lofigroup.seeyau.data.auth

import com.lofigroup.seeyau.data.auth.model.AccessRequest
import com.lofigroup.seeyau.data.auth.model.StartAuthRequest
import com.lofigroup.seeyau.data.auth.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

  @POST("/api/auth/verify")
  suspend fun authorize(@Body body: AccessRequest, @Query("token") token: String): Response<TokenResponse>

  @POST("/api/auth/start")
  suspend fun startAuth(@Body body: StartAuthRequest): Response<TokenResponse>

  @GET("/api/auth/check")
  suspend fun check(): Response<Unit>

}