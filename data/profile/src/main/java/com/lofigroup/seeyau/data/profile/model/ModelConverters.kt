package com.lofigroup.seeyau.data.profile.model

import com.lofigroup.backend_api.models.UpdateProfileRequest
import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.domain.profile.model.Profile

fun UserDto.toProfileDataModel() = ProfileDataModel(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toUserDto() = UserDto(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toProfileDataModel() = ProfileDataModel(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun ProfileDataModel.toProfile() = Profile(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toUpdateProfileRequest() = UpdateProfileRequest(
  name = name,
  imageUrl = imageUrl ?: ""
)