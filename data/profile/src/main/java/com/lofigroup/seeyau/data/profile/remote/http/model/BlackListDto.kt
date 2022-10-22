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
  val createdIn: Long
)

fun BlackListDto.toEntity() = BlacklistEntity(
  id = id,
  byWho = byWho,
  toWhom = toWhom,
  createdIn = createdIn
)