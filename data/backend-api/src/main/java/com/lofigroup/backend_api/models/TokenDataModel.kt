package com.lofigroup.backend_api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenDataModel(
  val token: String
)