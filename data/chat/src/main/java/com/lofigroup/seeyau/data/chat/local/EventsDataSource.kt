package com.lofigroup.seeyau.data.chat.local

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import kotlinx.coroutines.flow.Flow

interface EventsDataSource {
  fun onNewMessageEvent(message: ChatMessageDto)

  fun onChatIsReadEvent(response: ChatIsReadWsResponse)

  fun observe(chatId: Long): Flow<ChatEvent>
}