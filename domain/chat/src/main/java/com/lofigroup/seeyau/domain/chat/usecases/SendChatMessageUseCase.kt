package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke(chatMessageRequest: ChatMessageRequest) {
    repository.sendMessage(chatMessageRequest)
  }

}