package com.lofigroup.features.navigator_screen.model

import android.content.res.Resources
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.model.toUIMessage
import com.sillyapps.core_time.DateAndTime

data class PreviewMessage(
  val message: UIChatMessage,
  val positionInList: Int
) {
  companion object {
    private var previewCount = 0L

    fun getPreviewModel(
      id: Long = previewCount++,
      message: String = "Hello",
      dateTime: DateAndTime = DateAndTime("3 oct", "23:00"),
      positionInList: Int = 0
    ) = PreviewMessage(
      message = getPreviewMessage(id, authorIsMe = false, message, dateTime.date, dateTime.time, status = MessageStatus.READ),
      positionInList = positionInList
    )
  }
}

fun ChatMessage.toPreviewMessage(resources: Resources, positionInList: Int): PreviewMessage {
  return PreviewMessage(
    message = toUIMessage(resources, pos = positionInList),
    positionInList = positionInList
  )
}