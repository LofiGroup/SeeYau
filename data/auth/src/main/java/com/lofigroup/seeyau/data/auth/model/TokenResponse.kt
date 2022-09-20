package com.lofigroup.seeyau.data.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
  @Json(name = "access_token")
  val token: String
)