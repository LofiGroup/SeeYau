package com.lofigroup.seeyau.data.profile.remote.websocket.models

import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed interface ProfileWebsocketResponse {

  @JsonClass(generateAdapter = true)
  data class LikeIsUpdated(
    val id: Long,
    @Json(name = "by_who")
    val byWho: Long,
    @Json(name = "when")
    val updatedIn: Long,
    @Json(name = "is_liked")
    val isLiked: Boolean
  ) : ProfileWebsocketResponse

  companion object {
    val adapter: JsonAdapter<ProfileWebsocketResponse> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(ProfileWebsocketResponse::class.java, "type")
        .withSubtype(LikeIsUpdated::class.java, "like_is_updated")
    ).build().adapter(ProfileWebsocketResponse::class.java)
  }

}

fun ProfileWebsocketResponse.LikeIsUpdated.toLikeEntity() = LikeEntity(
  id = id,
  byWho = byWho,
  toWhom = 0,
  isLiked = isLiked,
  updatedIn = updatedIn
)