package com.lofigroup.seeyau.features.chat.model

import android.content.res.Resources
import com.lofigroup.seayau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.sillyapps.core_time.DateAndTime

sealed class UIChatMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val dateTime: DateAndTime,
  val status: MessageStatus
) {
  class Like(id: Long, authorIsMe: Boolean, dateTime: DateAndTime, status: MessageStatus): UIChatMessage(id, authorIsMe, dateTime, status)
  class Plain(val message: String, id: Long, authorIsMe: Boolean, dateTime: DateAndTime, status: MessageStatus): UIChatMessage(id, authorIsMe, dateTime, status)
}

fun ChatMessage.toPrivateMessage(resources: Resources): UIChatMessage {
  return when (this) {
    is ChatMessage.LikeMessage ->
      UIChatMessage.Like(
        authorIsMe = author == 0L,
        dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
        id = id,
        status = status,
      )
    is ChatMessage.PlainMessage ->
      UIChatMessage.Plain(
        authorIsMe = author == 0L,
        message = message,
        dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
        id = id,
        status = status,
      )
  }
}

fun getPreviewPrivateMessage(
  id: Long = 1,
  authorIsMe: Boolean = true,
  message: String = "Hello!",
  date: String = "Today",
  time: String = "14:20",
  status: MessageStatus = MessageStatus.READ
): UIChatMessage {
  return UIChatMessage.Plain(
    authorIsMe = authorIsMe,
    message = message,
    dateTime = DateAndTime(date, time),
    id = id,
    status = status
  )
}