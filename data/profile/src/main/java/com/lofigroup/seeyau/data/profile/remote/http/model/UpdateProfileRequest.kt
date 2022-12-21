package com.lofigroup.seeyau.data.profile.remote.http.model

import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

fun ProfileUpdate.toUpdateProfileForm() = mutableMapOf(
  Pair("name", (name ?: "").toRequestBody("text/plain".toMediaType()))
)

