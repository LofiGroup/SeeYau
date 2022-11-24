package com.lofigroup.seeyau.features.chat

import android.net.Uri
import com.lofigroup.seeyau.features.chat.model.ChatScreenCommand
import com.lofigroup.seeyau.features.chat.model.ChatScreenState
import kotlinx.coroutines.flow.Flow

interface ChatScreenStateHolder {

  fun getChatState(): Flow<ChatScreenState>

  fun getCommands(): Flow<ChatScreenCommand>

  fun sendMessage(mediaUri: Uri? = null)

  fun setMessage(message: String)

  fun onExit()

  fun onIgnoreUser()

}