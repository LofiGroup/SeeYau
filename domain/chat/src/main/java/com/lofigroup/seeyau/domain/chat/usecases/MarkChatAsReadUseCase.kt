package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import javax.inject.Inject

class MarkChatAsReadUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke(chatId: Long) {
    repository.markChatAsRead(chatId)
  }

}