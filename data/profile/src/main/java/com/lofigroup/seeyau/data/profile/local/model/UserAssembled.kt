package com.lofigroup.seeyau.data.profile.local.model

import com.lofigroup.seeyau.domain.profile.model.Like
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core_time.Time

data class UserAssembled(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long,
  val lastContact: Long,
  val likesCount: Int,

  val blacklistedYouAt: Long?,
  val likedYouAt: Long?,
  val likedAt: Long?
)

fun UserAssembled.toUser() = User(
  id = id,
  name = name,
  imageUrl = imageUrl,
  isOnline = lastConnection == Time.IS_ONLINE,
  lastConnection = lastConnection,
  isNear = lastContact > System.currentTimeMillis() - Time.m,
  blacklistedYou = blacklistedYouAt != null,
  likedYouAt = likedYouAt,
  likedAt = likedAt
)

fun UserAssembled.extractLike() = run {
  likedAt?.let {
    Like(
      id = 0,
      createdIn = it,
      userId = id
    )
  }
}