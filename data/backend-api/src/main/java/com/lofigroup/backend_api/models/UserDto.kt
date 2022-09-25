package com.lofigroup.backend_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
  @Json(name = "user_id")
  val id: Long,

  @Json(name = "name")
  val name: String,

  @Json(name = "img_url")
  val imageUrl: String?,

  @Json(name = "last_seen")
  val lastSeen: Long,

  @Json(name = "last_contact")
  val lastContact: Long
)