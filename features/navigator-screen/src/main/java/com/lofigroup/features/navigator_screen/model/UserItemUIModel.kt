package com.lofigroup.features.navigator_screen.model

import android.content.res.Resources
import com.lofigroup.domain.navigator.model.NearbyUser

data class UserItemUIModel(
  val id: Long,
  val imageUrl: String?,
  val name: String,
  val isOnline: Boolean,
  val isLikedByMe: Boolean,
  val likesCount: Int,
  val newMessages: List<PreviewMessage>,
  val newMessagesMapped: Map<String, List<PreviewMessage>>,
  val newMessagesCount: Int,
  val hasNewMessages: Boolean,
  val isSelected: Boolean,
  val messagesIsCollapsed: Boolean,
) {
  companion object {
    fun getPreviewModel(
      id: Long = 1L,
      imageUrl: String? = "",
      name: String = "Random",
      isOnline: Boolean = false,
      newMessages: List<PreviewMessage> = listOf(),
      isSelected: Boolean = false,
      messagesIsCollapsed: Boolean = false
    ): UserItemUIModel {
      val messages = newMessages.mapIndexed { index, chatMessage -> chatMessage.copy(positionInList = index) }
      return UserItemUIModel(
        id,
        imageUrl,
        name,
        isOnline,
        false,
        1,
        messages,
        messages.groupBy { it.message.dateTime.date },
        messages.size,
        messages.isNotEmpty(),
        isSelected = isSelected,
        messagesIsCollapsed = messagesIsCollapsed
      )
    }
  }
}

fun NearbyUser.toUIModel(
  isSelected: Boolean,
  oldValue: UserItemUIModel?,
  resources: Resources
): UserItemUIModel {
  val list = newMessages.mapIndexed { index, chatMessage ->  chatMessage.toPreviewMessage(resources, positionInList = index) }
  return UserItemUIModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    isLikedByMe = isLikedByMe,
    likesCount = likesCount,
    isOnline = isOnline,
    newMessages = list,
    newMessagesMapped = list.groupBy { it.message.dateTime.date },
    newMessagesCount = newMessages.size,
    hasNewMessages = newMessages.isNotEmpty(),
    isSelected = isSelected,
    messagesIsCollapsed = oldValue?.messagesIsCollapsed ?: false
  )
}

