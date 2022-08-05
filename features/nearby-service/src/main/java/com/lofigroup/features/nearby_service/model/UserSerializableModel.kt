package com.lofigroup.features.nearby_service.model

import com.lofigroup.domain.navigator.model.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSerializableModel(
  val id: Int,
  val name: String,
  val isNear: Boolean
)

fun UserSerializableModel.toUser(deviceId: String): User {
  return User(
    id = id,
    isNear = isNear,
    name = name,
    imageUrl = "",
    lastConnection = System.currentTimeMillis(),
    endpointId = deviceId
  )
}

fun User.toSerializableModel(): UserSerializableModel {
  return UserSerializableModel(
    id = id,
    name = name,
    isNear = isNear
  )
}