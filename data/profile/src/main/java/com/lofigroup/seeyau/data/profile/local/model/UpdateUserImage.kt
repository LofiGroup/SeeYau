package com.lofigroup.seeyau.data.profile.local.model

data class UpdateUserImage(
  val id: Long,
  val imageContentUri: String,
  val imageRemoteUrl: String,
)
