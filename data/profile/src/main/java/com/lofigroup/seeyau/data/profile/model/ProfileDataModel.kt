package com.lofigroup.seeyau.data.profile.model

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDataModel(
  val id: Long,
  val name: String,
  val imageUrl: String?
)

