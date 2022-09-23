package com.lofigroup.domain.navigator.model

data class NearbyUser(
  val id: Long,
  val name: String,
  val imageUrl: String?,
  val lastConnection: Long,
  val isNear: Boolean
)