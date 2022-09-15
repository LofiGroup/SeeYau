package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke(message: String, chatId: Long) {
    repository.sendMessage(ChatMessageRequest(
      message = message,
      chatId = chatId
    ))
  }

}