package com.lofigroup.data.navigator.local.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.domain.navigator.model.User
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

fun ProfileDataModel.toUser() = User(
  id = id,
  name = name,
  imageUrl = imgUrl,
  lastConnection = 0L
)