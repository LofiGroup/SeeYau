package com.lofigroup.seeyau.features.chat_screen.ui

import com.lofigroup.seeyau.features.chat_screen.model.ChatListScreenState
import kotlinx.coroutines.flow.Flow

interface ChatListScreenStateHolder {

  fun getState(): Flow<ChatListScreenState>

}