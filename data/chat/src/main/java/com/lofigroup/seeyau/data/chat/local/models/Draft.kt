package com.lofigroup.seeyau.data.chat.local.models

import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import com.lofigroup.seeyau.domain.chat.models.ChatDraftUpdate

data class Draft(
  val message: String,
  val createdIn: Long
)

fun ChatDraftUpdate.toDraftUpdate() = DraftUpdate(
  id = chatId,
  draft = Draft(
    message = message,
    createdIn = System.currentTimeMillis()
  )
)

fun Draft.toDomainModel() = ChatDraft(
  message = message,
  createdIn = createdIn
)