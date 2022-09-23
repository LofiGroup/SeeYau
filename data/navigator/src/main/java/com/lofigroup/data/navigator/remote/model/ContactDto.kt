package com.lofigroup.data.navigator.remote.model

import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.sillyapps.core_time.Time
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactDto(
  val id: Long,

  @Json(name = "name")
  val name: String,

  @Json(name = "img_url")
  val imageUrl: String?,

  @Json(name = "last_contact")
  val lastConnection: Long
)

fun ContactDto.toLocalDataModel() = UserEntity(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = lastConnection
)

fun UserEntity.toNearbyUser() = NearbyUser(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = lastConnection,
  isNear = System.currentTimeMillis() - lastConnection < 1 * Time.m
)
