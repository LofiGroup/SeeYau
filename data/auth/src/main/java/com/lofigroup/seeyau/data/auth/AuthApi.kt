package com.lofigroup.seeyau.data.auth

import com.lofigroup.seeyau.data.auth.model.AccessRequest
import com.lofigroup.seeyau.data.auth.model.StartAuthRequest
import com.lofigroup.seeyau.data.auth.model.TokenResponse
import com.lofigroup.seeyau.data.auth.model.VerifyResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

  @POST("/api/auth/verify")
  suspend fun authorize(@Body body: AccessRequest, @Query("token") token: String): Response<VerifyResponse>

  @POST("/api/auth/start")
  suspend fun startAuth(@Body body: StartAuthRequest): Response<TokenResponse>

  @GET("/api/auth/check")
  suspend fun check(): Response<Unit>

  @Multipart
  @POST("/api/auth/quick-auth")
  suspend fun quickAuth(@Part image: MultipartBody.Part?): Response<TokenResponse>

}