package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import javax.inject.Inject

class GetUserIdByChatIdUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke(chatId: Long): Long? {
    return repository.getUserIdByChatId(chatId)
  }

}