package com.lofigroup.seeyau.data.profile

import com.lofigroup.seeyau.data.profile.model.UpdateProfileRequest
import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {

  @GET("/api/profiles/me")
  suspend fun getProfile(): Response<UserDto>

  @PUT("/api/profiles/me")
  suspend fun updateProfile(@Body body: UpdateProfileRequest): Response<Unit>

}