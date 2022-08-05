package com.lofigroup.data.navigator.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lofigroup.domain.navigator.model.User

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: Int,
  val endpointId: String,
  val name: String,
  val isNear: Boolean,
  val lastConnection: Long
)

fun User.toUserEntity(): UserEntity {
  return UserEntity(
    id = id,
    endpointId = endpointId,
    name = name,
    isNear = isNear,
    lastConnection = lastConnection
  )
}

fun UserEntity.toUser(): User {
  return User(
    id = id,
    name = name,
    isNear = isNear,
    imageUrl = "",
    lastConnection = lastConnection,
    endpointId = endpointId
  )
}