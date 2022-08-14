package com.lofigroup.data.navigator.remote.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.domain.navigator.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

fun UserDto.toUserEntity() = UserEntity(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = System.currentTimeMillis()
)