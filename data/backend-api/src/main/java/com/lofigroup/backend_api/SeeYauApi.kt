package com.lofigroup.backend_api

import com.lofigroup.backend_api.models.AccessRequest
import com.lofigroup.backend_api.models.AccessResponse
import com.lofigroup.backend_api.models.UpdateProfileRequest
import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response
import retrofit2.http.*

interface SeeYauApi {

  @POST("/api/auth/login")
  suspend fun login(@Body body: AccessRequest): Response<AccessResponse>

  @POST("/api/auth/register")
  suspend fun register(@Body body: AccessRequest): Response<AccessResponse>

  @GET("/api/auth/check")
  suspend fun check(): Response<Unit>

  @GET("/api/profiles/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

  @GET("/api/profiles/me")
  suspend fun getMe(): Response<UserDto>

  @PUT("/api/profiles/me")
  suspend fun updateMe(@Body body: UpdateProfileRequest): Response<Unit>

}