package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(chatId: Long): Flow<Chat> {
    return repository.observeChat(chatId)
  }

}