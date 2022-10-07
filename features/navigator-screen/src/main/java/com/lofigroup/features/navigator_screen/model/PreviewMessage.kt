package com.lofigroup.features.navigator_screen.model

import android.content.res.Resources
import com.lofigroup.seayau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.DateAndTime

data class PreviewMessage(
  val id: Long,
  val message: String,
  val dateTime: DateAndTime
)

fun ChatMessage.toPrivateMessage(resources: Resources): PreviewMessage {
  return PreviewMessage(
    message = message,
    dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
    id = id
  )
}