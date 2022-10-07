package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatsUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(): Flow<List<ChatBrief>> {
    return repository.observeChats()
  }

}