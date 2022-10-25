package com.lofigroup.seeyau.data.profile.remote.websocket.models

import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity
import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouAreBlacklisted(
  val id: Long,
  @Json(name = "user_id")
  val userId: Long,
  @Json(name = "when")
  val createdIn: Long,
  @Json(name = "is_active")
  val isActive: Boolean
) : ProfileWebsocketResponse {
  companion object {
    const val type = "you_are_blacklisted"
  }
}

fun YouAreBlacklisted.toBlacklistDto() = BlackListDto(
  id = id,
  byWho = userId,
  toWhom = 0,
  createdIn = createdIn,
  isActive = isActive
)