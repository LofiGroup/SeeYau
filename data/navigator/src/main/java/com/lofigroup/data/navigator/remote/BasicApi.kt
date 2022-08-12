package com.lofigroup.data.navigator.remote

import retrofit2.http.GET

interface BasicApi {

  @GET("/users/")
  suspend fun getUser(id: Long)

}