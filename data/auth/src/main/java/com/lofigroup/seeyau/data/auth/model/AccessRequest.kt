package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.seeyau.domain.auth.model.Access
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessRequest(
  @Json(name = "email")
  val email: String,
  @Json(name = "password")
  val password: String
)

fun Access.toAccessRequest(): AccessRequest {
  return AccessRequest(
    email = email,
    password = password
  )
}
