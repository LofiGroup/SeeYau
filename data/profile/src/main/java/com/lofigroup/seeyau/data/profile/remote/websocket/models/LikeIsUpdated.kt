package com.lofigroup.seeyau.data.profile.remote.websocket.models

import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeIsUpdated(
  val id: Long,
  @Json(name = "by_who")
  val byWho: Long,
  @Json(name = "when")
  val updatedIn: Long,
  @Json(name = "is_liked")
  val isLiked: Boolean
) : ProfileWebsocketResponse {
  companion object {
    const val type = "like_is_updated"
  }
}

fun LikeIsUpdated.toLikeEntity() = LikeEntity(
  id = id,
  byWho = byWho,
  toWhom = 0,
  isLiked = isLiked,
  updatedIn = updatedIn
)