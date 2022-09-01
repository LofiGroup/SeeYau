package com.lofigroup.data.navigator.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core_time.Time

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long
)

fun User.toUserEntity(): UserEntity {
  return UserEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    lastConnection = lastConnection
  )
}

fun UserEntity.toDomainModel(): User {
  return User(
    id = id,
    name = name,
    imageUrl = imageUrl,
    lastConnection = lastConnection,
    isNear = System.currentTimeMillis() - lastConnection < 1 * Time.m
  )
}