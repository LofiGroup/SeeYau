package com.lofigroup.data.navigator

import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NavigatorApi {

  @GET("/api/profiles/contacts")
  suspend fun getContacts(): Response<List<UserDto>>

  @POST("/api/profiles/contact/{user_id}")
  suspend fun contactedWithUser(@Path("user_id") id: Long): Response<UserDto>

}