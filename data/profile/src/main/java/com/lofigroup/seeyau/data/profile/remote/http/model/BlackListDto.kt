package com.lofigroup.seeyau.data.profile.remote.http.model

import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlackListDto(
  val id: Long,
  @Json(name = "by_who")
  val byWho: Long,
  @Json(name = "to_whom")
  val toWhom: Long,
  @Json(name = "when")
  val createdIn: Long,
  @Json(name = "is_active")
  val isActive: Boolean
)

fun BlackListDto.toEntity(myId: Long) = run {
  var byWho = byWho
  var toWhom = toWhom

  if (byWho == myId) byWho = 0
  else toWhom = 0

  BlacklistEntity(
    id = id,
    byWho = byWho,
    toWhom = toWhom,
    createdIn = createdIn
  )
}