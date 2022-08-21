package com.lofigroup.data.navigator.remote

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.domain.navigator.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NavigatorApi {

  @GET("/api/profiles/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

}