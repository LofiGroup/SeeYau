package com.lofigroup.seeyau.data.auth.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirebaseToken(
  val token: String
)
