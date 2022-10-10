package com.lofigroup.features.navigator_screen.model

import android.content.res.Resources
import com.lofigroup.seayau.common.ui.getLocalizedDatedAndTimeFromMillis
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.sillyapps.core_time.DateAndTime

data class PreviewMessage(
  val id: Long,
  val message: String,
  val dateTime: DateAndTime,
  val positionInList: Int
) {
  companion object {
    private var previewCount = 0L

    fun getPreviewModel(
      id: Long = previewCount++,
      message: String = "Hello",
      dateTime: DateAndTime = DateAndTime("3 oct", "23:00"),
      positionInList: Int = 0
    ) = PreviewMessage(id, message, dateTime, positionInList)
  }
}

fun ChatMessage.toPrivateMessage(resources: Resources, positionInList: Int): PreviewMessage {
  return PreviewMessage(
    message = message,
    dateTime = getLocalizedDatedAndTimeFromMillis(createdIn, resources),
    id = id,
    positionInList = positionInList
  )
}