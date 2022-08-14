package com.lofigroup.backend_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessResponse(
  @Json(name = "access_token")
  val token: String,
  @Json(name = "token_type")
  val tokenType: String
)