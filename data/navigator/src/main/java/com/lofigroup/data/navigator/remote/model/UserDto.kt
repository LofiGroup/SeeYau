package com.lofigroup.data.navigator.remote.model

import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.domain.navigator.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
  @Json(name = "id")
  val id: Long,

  @Json(name = "name")
  val name: String,

  @Json(name = "img_url")
  val imageUrl: String?
)

fun UserDto.toUser() = User(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = 0L
)

fun UserDto.toUserEntity() = UserEntity(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = System.currentTimeMillis()
)