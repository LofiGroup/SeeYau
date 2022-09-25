package com.lofigroup.seeyau.domain.chat.usecases

import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
  private val repository: ChatRepository
) {

  operator fun invoke(id: Long): Flow<Chat> {
    return repository.getChat(id)
  }

}