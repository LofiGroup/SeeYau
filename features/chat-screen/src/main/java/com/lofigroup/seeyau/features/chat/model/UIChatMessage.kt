package com.lofigroup.seeyau.features.chat.model

import android.content.res.Resources
import com.lofigroup.seeyau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.sillyapps.core_time.DateAndTime

data class UIChatMessage(
  val id: Long,
  val authorIsMe: Boolean,
  val dateTime: DateAndTime,
  val status: MessageStatus,
  val message: String,
  val type: UIMessageType,
  val pos: Int
)

fun ChatMessage.toUIMessage(resources: Resources, pos: Int): UIChatMessage {
  return UIChatMessage(
    authorIsMe = author == 0L,
    dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
    id = id,
    status = status,
    message = message,
    type = type.toUIMessageType(),
    pos = pos
  )
}

fun getPreviewMessage(
  id: Long = 1,
  authorIsMe: Boolean = true,
  message: String = "Hello!",
  date: String = "Today",
  time: String = "14:20",
  status: MessageStatus = MessageStatus.READ,
  type: MessageType = MessageType.Plain,
  pos: Int = 0
): UIChatMessage {
  return UIChatMessage(
    authorIsMe = authorIsMe,
    message = message,
    dateTime = DateAndTime(date, time),
    id = id,
    status = status,
    type = type.toUIMessageType(),
    pos = pos
  )
}