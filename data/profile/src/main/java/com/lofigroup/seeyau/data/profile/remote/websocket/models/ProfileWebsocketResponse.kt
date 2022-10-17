package com.lofigroup.seeyau.data.profile.remote.websocket.models

import com.lofigroup.seeyau.data.profile.remote.http.model.LikeDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed interface ProfileWebsocketResponse {

  data class LikeIsUpdated(val like: LikeDto): ProfileWebsocketResponse

  companion object {
    val adapter = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(ProfileWebsocketResponse::class.java, "type")
        .withSubtype(LikeIsUpdated::class.java, "like_is_updated")
    ).build().adapter(ProfileWebsocketResponse::class.java)
  }

}