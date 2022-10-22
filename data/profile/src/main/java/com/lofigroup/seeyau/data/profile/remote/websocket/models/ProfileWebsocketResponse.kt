package com.lofigroup.seeyau.data.profile.remote.websocket.models

import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed interface ProfileWebsocketResponse {

  companion object {
    val adapter: JsonAdapter<ProfileWebsocketResponse> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(ProfileWebsocketResponse::class.java, "type")
        .withSubtype(LikeIsUpdated::class.java, LikeIsUpdated.type)
        .withSubtype(YouAreBlacklisted::class.java, YouAreBlacklisted.type)
    ).build().adapter(ProfileWebsocketResponse::class.java)
  }
}