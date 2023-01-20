package com.lofigroup.seeyau.common.ui.main_screen_event_channel.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import java.io.IOException

sealed interface MainScreenEvent {
  @JsonClass(generateAdapter = true)
  data class OpenChat(val chatId: Long) : MainScreenEvent

  @JsonClass(generateAdapter = true)
  class OpenApp: MainScreenEvent

  companion object {
    private val adapter: JsonAdapter<MainScreenEvent> = Moshi.Builder().add(
      PolymorphicJsonAdapterFactory.of(MainScreenEvent::class.java, "eventType")
        .withSubtype(OpenChat::class.java, "OPEN_CHAT")
        .withSubtype(OpenApp::class.java, "OPEN_APP"))
      .build().adapter(MainScreenEvent::class.java)

    fun deserialize(data: String?): MainScreenEvent? {
      if (data == null) return null

      return try {
        adapter.fromJson(data)
      } catch (e: IOException) {
        null
      }
    }

    fun serialize(event: MainScreenEvent): String {
      return adapter.toJson(event)
    }
  }
}
