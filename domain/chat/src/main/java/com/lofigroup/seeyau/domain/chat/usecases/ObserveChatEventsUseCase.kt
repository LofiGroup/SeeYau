package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatEventsUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(chatId: Long): Flow<ChatEvent> {
    return repository.observeChatEvents(chatId)
  }

}