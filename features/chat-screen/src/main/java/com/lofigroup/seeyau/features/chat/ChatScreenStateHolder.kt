package com.lofigroup.seeyau.features.chat

import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import kotlinx.coroutines.flow.Flow

interface ChatScreenStateHolder {

  fun getChatState(): Flow<ChatScreenState>

  fun sendMessage()

  fun setMessage(message: String)

}