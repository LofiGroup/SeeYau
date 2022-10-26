package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.backend_api.models.TokenDataModel
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyResponse(
  @Json(name = "access_token")
  val token: String,
  @Json(name = "exists")
  val exists: Boolean
)

fun VerifyResponse.toTokenDataModel() = TokenDataModel(
  token = token
)

fun VerifyResponse.toAuthResponse() = AuthResponse(
  exists = exists
)