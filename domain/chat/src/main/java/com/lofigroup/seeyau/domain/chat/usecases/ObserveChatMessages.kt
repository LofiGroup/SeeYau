package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatMessages @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(chatId: Long): Flow<List<ChatMessage>> {
    return repository.observeChatMessages(chatId)
  }

}