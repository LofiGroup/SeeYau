package com.lofigroup.seeyau.data.profile

import com.lofigroup.seeyau.data.profile.model.UpdateProfileRequest
import com.lofigroup.backend_api.models.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

  @GET("/api/profiles/me")
  suspend fun getProfile(): Response<UserDto>

  @Multipart
  @POST("/api/profiles/me")
  suspend fun updateProfile(
    @PartMap form: MutableMap<String, RequestBody>,
    @Part image: MultipartBody.Part?
  ): Response<UserDto>

}