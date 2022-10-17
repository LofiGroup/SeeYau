package com.lofigroup.seeyau.features.chat.model

import android.content.res.Resources
import com.lofigroup.seayau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.DateAndTime

sealed class UIChatMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val dateTime: DateAndTime,
  val isRead: Boolean
) {
  class Like(id: Long, authorIsMe: Boolean, dateTime: DateAndTime, isRead: Boolean): UIChatMessage(id, authorIsMe, dateTime, isRead)
  class Plain(val message: String, id: Long, authorIsMe: Boolean, dateTime: DateAndTime, isRead: Boolean): UIChatMessage(id, authorIsMe, dateTime, isRead)
}

fun ChatMessage.toPrivateMessage(resources: Resources): UIChatMessage {
  return when (this) {
    is ChatMessage.LikeMessage ->
      UIChatMessage.Like(
        authorIsMe = author == 0L,
        dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
        id = id,
        isRead = isRead,
      )
    is ChatMessage.PlainMessage ->
      UIChatMessage.Plain(
        authorIsMe = author == 0L,
        message = message,
        dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
        id = id,
        isRead = isRead,
      )
  }
}

fun getPreviewPrivateMessage(
  id: Long = 1,
  authorIsMe: Boolean = true,
  message: String = "Hello!",
  date: String = "Today",
  time: String = "14:20",
  isRead: Boolean = true
): UIChatMessage {
  return UIChatMessage.Plain(
    authorIsMe = authorIsMe,
    message = message,
    dateTime = DateAndTime(date, time),
    id = id,
    isRead = isRead
  )
}