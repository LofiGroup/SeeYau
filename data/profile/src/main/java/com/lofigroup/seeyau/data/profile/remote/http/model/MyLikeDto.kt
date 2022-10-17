package com.lofigroup.seeyau.data.profile.remote.http.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyLikeDto(
  val id: Long,
  val whom: Long,
  @Json(name = "when")
  val updatedIn: Long,
  @Json(name = "is_liked")
  val isLiked: Boolean
)