package com.lofigroup.data.navigator.remote

import com.lofigroup.data.navigator.remote.model.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SeeYauApi {

  @GET("/users/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

  @GET("users/me")
  suspend fun getMe(): Response<UserDto>

}