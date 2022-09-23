package com.lofigroup.seeyau.domain.profile.model

data class User(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long,
  val isNear: Boolean
)