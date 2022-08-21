package com.lofigroup.seeyau.data.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
  @Json(name = "name")
  val name: String,
  @Json(name = "img_url")
  val imageUrl: String
)