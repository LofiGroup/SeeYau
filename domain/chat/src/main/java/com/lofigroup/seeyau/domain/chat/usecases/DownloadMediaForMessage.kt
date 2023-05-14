package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import javax.inject.Inject

class DownloadMediaForMessage @Inject constructor(
  private val repository: ChatRepository
){

  operator fun invoke(messageId: Long) {
    repository.downloadMediaForMessage(messageId)
  }

}