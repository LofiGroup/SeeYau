package com.lofigroup.seeyau.features.chat.model

import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.getLocalDateAndTimeFromMillis

data class PrivateMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val message: String,
  val date: String,
  val time: String,
  val isRead: Boolean,

  val positionInList: Int
)

fun ChatMessage.toPrivateMessage(positionInList: Int): PrivateMessage {
  val (time, date) = getLocalDateAndTimeFromMillis(createdIn)

  return PrivateMessage(
    authorIsMe = author == 0L,
    message = message,
    time = time,
    date = date,
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
    time = time,
    date = date,
    id = id,
    isRead = isRead,
    positionInList = 0
  )
}