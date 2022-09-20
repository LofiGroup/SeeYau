package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.seeyau.domain.auth.model.StartAuth
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartAuthRequest(
  val name: String,
  @Json(name = "phone_number")
  val phoneNumber: String
)

fun StartAuth.toStartAuthRequest() =
  StartAuthRequest(
    name = name,
    phoneNumber = phoneNumber
  )