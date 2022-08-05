package com.lofigroup.domain.navigator.model

data class User(
  val id: Int,
  val endpointId: String,
  val name: String,
  val isNear: Boolean,
  val imageUrl: String,
  val lastConnection: Long
)