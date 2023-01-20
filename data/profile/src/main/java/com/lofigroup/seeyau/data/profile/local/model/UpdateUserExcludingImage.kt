package com.lofigroup.seeyau.data.profile.local.model

data class UpdateUserExcludingImage(
  val id: Long,
  val name: String,
  val lastConnection: Long,
  val lastContact: Long,
  val likesCount: Int
)

fun UserEntity.toUpdateUserExcludingImage() = UpdateUserExcludingImage(
  id = id,
  name = name,
  lastConnection = lastConnection,
  lastContact = lastContact,
  likesCount = likesCount
)
