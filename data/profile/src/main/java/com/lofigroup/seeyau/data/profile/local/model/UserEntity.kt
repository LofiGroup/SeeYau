package com.lofigroup.seeyau.data.profile.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core_time.Time

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long,
  val lastContact: Long,
  val likesCount: Int
)

fun UserEntity.toDomainModel(): User {
  return User(
    id = id,
    name = name,
    imageUrl = imageUrl,
    lastConnection = lastConnection,
    isOnline = lastConnection == Time.IS_ONLINE,
    isNear = System.currentTimeMillis() - lastContact < 1 * Time.m,

    blacklistedYou = false,
    likedYouAt = null,
    likedAt = null,
    lastContact = lastContact,
    likesCount = likesCount
  )
}

fun UserDto.toUserEntity() = UserEntity(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = lastSeen,
  lastContact = lastContact,
  likesCount = likesCount
)