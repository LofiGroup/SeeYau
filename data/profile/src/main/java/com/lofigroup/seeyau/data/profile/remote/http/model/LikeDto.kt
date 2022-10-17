package com.lofigroup.seeyau.data.profile.remote.http.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeDto(
  val id: Long,
  @Json(name = "by_who")
  val byWho: Long,
  @Json(name = "to_whom")
  val toWhom: Long,
  @Json(name = "when")
  val updatedIn: Long,
  @Json(name = "is_liked")
  val isLiked: Boolean
)
