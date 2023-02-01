package com.lofigroup.seeyau.data.profile.local.model

import com.lofigroup.seeyau.domain.profile.model.Like
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core_time.Time

data class UserAssembled(
  val id: Long,
  val name: String,
  val imageContentUri: String?,
  val lastConnection: Long,
  val lastContact: Long,
  val likesCount: Int,

  val likedYouAt: Long?,
  val likedAt: Long?
)

fun UserAssembled.toUser() = User(
  id = id,
  name = name,
  imageUrl = imageContentUri,
  isOnline = lastConnection == Time.IS_ONLINE,
  lastConnection = lastConnection,
  isNear = lastContact > System.currentTimeMillis() - Time.m,
  likedYouAt = likedYouAt,
  likedAt = likedAt,
  lastContact = lastContact,
  likesCount = likesCount
)

fun UserAssembled.extractLike() = run {
  likedYouAt?.let {
    Like(
      id = 0,
      createdIn = it,
      userId = id
    )
  }
}