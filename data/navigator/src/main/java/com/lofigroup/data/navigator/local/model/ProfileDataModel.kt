package com.lofigroup.data.navigator.local.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDataModel(
  val id: Long,
  val name: String
)
