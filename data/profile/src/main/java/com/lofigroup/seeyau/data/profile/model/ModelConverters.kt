package com.lofigroup.seeyau.data.profile.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.seeyau.domain.profile.model.Profile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

fun Profile.toUserDto() = UserDto(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toUpdateProfileForm() = mutableMapOf(
  Pair("name", name.toRequestBody("text/plain".toMediaType()))
)

fun UserEntity.toProfile() = Profile(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toUserEntity() = UserEntity(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = 0L
)