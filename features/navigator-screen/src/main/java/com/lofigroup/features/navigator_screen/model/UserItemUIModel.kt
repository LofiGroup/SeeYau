package com.lofigroup.features.navigator_screen.model

import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.Time

data class UserItemUIModel(
  val id: Long,
  val imageUrl: String?,
  val name: String,
  val isOnline: Boolean,
  val newMessages: List<ChatMessage>
) {
  companion object {
    fun getPreviewModel(
      id: Long = 1L,
      imageUrl: String? = "",
      name: String = "Random",
      isOnline: Boolean = false,
      newMessages: List<ChatMessage> = listOf()
    ) =
      UserItemUIModel(id, imageUrl, name, isOnline, newMessages)
  }
}

fun NearbyUser.toUIModel(): UserItemUIModel {
  return UserItemUIModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    isOnline = isOnline,
    newMessages = listOf()
  )
}

