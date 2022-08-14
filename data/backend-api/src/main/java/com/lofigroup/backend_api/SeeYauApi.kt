package com.lofigroup.backend_api

import com.lofigroup.backend_api.models.AccessRequest
import com.lofigroup.backend_api.models.AccessResponse
import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SeeYauApi {

  @POST("/login/")
  suspend fun login(@Body body: AccessRequest): Response<AccessResponse>

  @POST("/register/")
  suspend fun register(@Body body: AccessRequest): Response<AccessResponse>

  @GET("/users/{user_id}")
  suspend fun getUser(@Path("user_id") id: Long): Response<UserDto>

  @GET("users/me")
  suspend fun getMe(): Response<UserDto>

}