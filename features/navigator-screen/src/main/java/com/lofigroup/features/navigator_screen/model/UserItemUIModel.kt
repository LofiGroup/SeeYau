package com.lofigroup.features.navigator_screen.model

import android.content.res.Resources
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.Time

data class UserItemUIModel(
  val id: Long,
  val imageUrl: String?,
  val name: String,
  val isOnline: Boolean,
  val newMessages: List<PreviewMessage>,
  val hasNewMessages: Boolean,
  val isSelected: Boolean,
  val messagesIsCollapsed: Boolean
) {
  companion object {
    fun getPreviewModel(
      id: Long = 1L,
      imageUrl: String? = "",
      name: String = "Random",
      isOnline: Boolean = false,
      newMessages: List<PreviewMessage> = listOf(),
      isSelected: Boolean = false
    ) =
      UserItemUIModel(id, imageUrl, name, isOnline, newMessages, newMessages.isNotEmpty(), isSelected = isSelected, messagesIsCollapsed = false)
  }
}

fun NearbyUser.toUIModel(
  isSelected: Boolean,
  oldValue: UserItemUIModel?,
  resources: Resources
): UserItemUIModel {
  return UserItemUIModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    isOnline = isOnline,
    newMessages = newMessages.map { it.toPrivateMessage(resources) },
    hasNewMessages = newMessages.isNotEmpty(),
    isSelected = isSelected,
    messagesIsCollapsed = oldValue?.messagesIsCollapsed ?: false
  )
}

