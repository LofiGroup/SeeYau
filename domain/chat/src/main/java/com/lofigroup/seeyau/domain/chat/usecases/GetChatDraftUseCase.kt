package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import javax.inject.Inject

class GetChatDraftUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke(chatId: Long): ChatDraft? {
    return repository.getChatDraft(chatId)
  }
}