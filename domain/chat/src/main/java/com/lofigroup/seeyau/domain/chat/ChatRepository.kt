package com.lofigroup.seeyau.domain.chat

import com.lofigroup.seeyau.domain.chat.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

  suspend fun pullData()

  fun getChats(): Flow<List<Chat>>

}