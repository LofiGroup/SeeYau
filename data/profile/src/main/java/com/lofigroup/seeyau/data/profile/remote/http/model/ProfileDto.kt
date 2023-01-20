package com.lofigroup.seeyau.data.profile.remote.http.model

import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDto(
  @Json(name = "user_id")
  val id: Long,

  @Json(name = "name")
  val name: String,

  @Json(name = "img_url")
  val imageUrl: String?
)

fun ProfileDto.toUserEntity() = UserEntity(
  id = 0,
  name = name,
  imageContentUri = imageUrl,
  lastConnection = 0L,
  lastContact = 0L,
  likesCount = 0
)