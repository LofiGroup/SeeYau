package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(id: Long): Flow<Chat> {
    return repository.getChat(id)
  }

}