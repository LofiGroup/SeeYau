package com.lofigroup.seeyau.data.profile

import com.lofigroup.backend_api.models.UpdateProfileRequest
import com.lofigroup.backend_api.models.UserDto
import retrofit2.Response

interface ProfileApi {

  suspend fun getProfile(): Response<UserDto>

  suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Response<Unit>

}