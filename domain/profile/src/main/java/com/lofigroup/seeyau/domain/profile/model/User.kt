package com.lofigroup.seeyau.domain.profile.model

data class User(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val isOnline: Boolean,
  val lastConnection: Long,
  val isNear: Boolean,

  val blacklistedYou: Boolean = false,
  val likedYouAt: Long? = null,
  val likedAt: Long? = null
)