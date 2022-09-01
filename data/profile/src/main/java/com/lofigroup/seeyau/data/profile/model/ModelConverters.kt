package com.lofigroup.seeyau.data.profile.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.seeyau.domain.profile.model.Profile

fun Profile.toUserDto() = UserDto(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toUpdateProfileRequest() = UpdateProfileRequest(
  name = name,
  imageUrl = imageUrl ?: ""
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