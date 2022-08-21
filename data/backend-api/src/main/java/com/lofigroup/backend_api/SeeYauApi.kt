package com.lofigroup.backend_api

import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response
import retrofit2.http.*

interface SeeYauApi {



  @GET("/api/profiles/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>



}