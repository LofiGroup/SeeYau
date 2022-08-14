package com.lofigroup.seeyau.data.profile.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDataModel(
  val id: Long,
  val name: String,
  val imgUrl: String?
)

fun UserDto.toProfileDataModel() = ProfileDataModel(
  id = id,
  name = name,
  imgUrl = imageUrl
)

fun ProfileDataModel.toProfile() = Profile(
  id = id,
  name = name,
  imageUrl = imgUrl
)