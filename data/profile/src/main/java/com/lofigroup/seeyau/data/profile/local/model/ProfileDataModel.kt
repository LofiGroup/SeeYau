package com.lofigroup.seeyau.data.profile.local.model

import com.lofigroup.seeyau.domain.profile.model.Profile
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDataModel(
  val id: Long,
  val name: String,
  val imageUrl: String?
)

fun UserEntity.toProfile() = Profile(
  id = id,
  name = name,
  imageUrl = imageUrl
)