package com.lofigroup.features.navigator_screen.model

import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class UserItemUIModel(
  val id: Long,
  val imageUrl: String?,
  val name: String,
  val isNear: Boolean,
  val isOnline: Boolean,
  val newMessages: List<ChatMessage>
) {
  companion object {
    fun getPreviewModel(
      id: Long = 1L,
      imageUrl: String? = "",
      name: String = "Random",
      isNear: Boolean = true,
      isOnline: Boolean = false,
      newMessages: List<ChatMessage> = listOf()
    ) =
      UserItemUIModel(id, imageUrl, name, isNear, isOnline, newMessages)
  }
}

fun NearbyUser.toUIModel(): UserItemUIModel {
  return UserItemUIModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    isNear = isNear,
    isOnline = lastConnection == 0L,
    newMessages = listOf()
  )
}

