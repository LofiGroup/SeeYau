package com.lofigroup.seeyau.data.profile.remote.http

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto
import com.lofigroup.seeyau.data.profile.remote.http.model.LikeDto
import com.lofigroup.seeyau.data.profile.remote.http.model.ProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

  @GET("/api/profiles/contact/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

  @GET("/api/profiles/contacts")
  suspend fun getContacts(): Response<List<UserDto>>

  @GET("/api/profiles/me")
  suspend fun getProfile(): Response<ProfileDto>

  @Multipart
  @POST("/api/profiles/me")
  suspend fun updateProfile(
    @PartMap form: MutableMap<String, RequestBody>,
    @Part image: MultipartBody.Part?
  ): Response<ProfileDto>

  @GET("/api/profiles/likes")
  suspend fun getLikes(@Query("from_date") fromDate: Long): Response<List<LikeDto>>

  @POST("/api/profiles/like/{user_id}")
  suspend fun likeUser(@Path("user_id") id: Long): Response<LikeDto>

  @POST("/api/profiles/unlike/{user_id}")
  suspend fun unlikeUser(@Path("user_id") id: Long): Response<Unit>

  @POST("/api/profiles/blacklist-user/{user_id}")
  suspend fun blackListUser(@Path("user_id") id: Long): Response<BlackListDto>

  @GET("/api/profiles/get-blacklist")
  suspend fun getBlackList(@Query("from_date") fromDate: Long): Response<List<BlackListDto>>

}