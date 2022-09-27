package com.lofigroup.seeyau.data.profile.remote

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.data.profile.remote.model.ProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

  @GET("/api/profiles/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

  @GET("/api/profiles/me")
  suspend fun getProfile(): Response<ProfileDto>

  @Multipart
  @POST("/api/profiles/me")
  suspend fun updateProfile(
    @PartMap form: MutableMap<String, RequestBody>,
    @Part image: MultipartBody.Part?
  ): Response<ProfileDto>

}