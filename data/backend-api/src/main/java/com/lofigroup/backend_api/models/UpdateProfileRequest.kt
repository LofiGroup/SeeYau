package com.lofigroup.backend_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
  @Json(name = "id")
  val id: Long,
  @Json(name = "name")
  val name: String,
  @Json(name = "image_url")
  val imageUrl: String
)
