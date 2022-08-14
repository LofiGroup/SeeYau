package com.lofigroup.data.navigator.remote

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.domain.navigator.model.User
import retrofit2.Response

interface NavigatorApi {

  suspend fun getMe(): Response<UserDto>

  suspend fun getUser(id: Long): Response<UserDto>

}