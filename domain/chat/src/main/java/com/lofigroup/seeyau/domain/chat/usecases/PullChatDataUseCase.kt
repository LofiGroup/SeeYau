package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatNewMessages
import javax.inject.Inject

class PullChatDataUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  suspend operator fun invoke() {
    return repository.pullData()
  }

}