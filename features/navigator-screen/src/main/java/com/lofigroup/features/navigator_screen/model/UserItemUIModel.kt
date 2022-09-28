package com.lofigroup.features.navigator_screen.model

import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.Time

data class UserItemUIModel(
  val id: Long,
  val imageUrl: String?,
  val name: String,
  val isNear: Boolean,
  val isOnline: Boolean,
  val lastConnection: Long,
  val newMessages: List<ChatMessage>
) {
  companion object {
    fun getPreviewModel(
      id: Long = 1L,
      imageUrl: String? = "",
      name: String = "Random",
      isNear: Boolean = true,
      isOnline: Boolean = false,
      lastConnection: Long = 0L,
      newMessages: List<ChatMessage> = listOf()
    ) =
      UserItemUIModel(id, imageUrl, name, isNear, isOnline, lastConnection, newMessages)
  }
}

fun NearbyUser.toUIModel(): UserItemUIModel {
  return UserItemUIModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    isNear = isNear,
    isOnline = lastConnection == Time.IS_ONLINE,
    lastConnection = lastConnection,
    newMessages = listOf()
  )
}

