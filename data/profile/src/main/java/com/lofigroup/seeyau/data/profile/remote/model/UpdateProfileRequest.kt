package com.lofigroup.seeyau.data.profile.remote.model

import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
  @Json(name = "name")
  val name: String?
)

fun ProfileUpdate.toUpdateProfileForm() = mutableMapOf(
  Pair("name", (name ?: "").toRequestBody("text/plain".toMediaType()))
)

