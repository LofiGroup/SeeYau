package com.lofigroup.seeyau.features.chat.model

import android.content.res.Resources
import com.lofigroup.seayau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.DateAndTime

data class PrivateMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val message: String,
  val dateTime: DateAndTime,
  val isRead: Boolean,

  val positionInList: Int
)

fun ChatMessage.toPrivateMessage(positionInList: Int, resources: Resources): PrivateMessage {
  return PrivateMessage(
    authorIsMe = author == 0L,
    message = message,
    dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
    id = id,
    isRead = isRead,
    positionInList = positionInList
  )
}

fun getPreviewPrivateMessage(
  id: Long = 1,
  authorIsMe: Boolean = true,
  message: String = "Hello!",
  date: String = "Today",
  time: String = "14:20",
  isRead: Boolean = true
): PrivateMessage {
  return PrivateMessage(
    authorIsMe = authorIsMe,
    message = message,
    dateTime = DateAndTime(date, time),
    id = id,
    isRead = isRead,
    positionInList = 0
  )
}