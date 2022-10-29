package com.lofigroup.seeyau.data.chat.local

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import kotlinx.coroutines.flow.Flow

interface EventsDataSource {
  fun onNewMessageEvent(event: NewChatMessage)

  fun onChatIsReadEvent(response: ChatIsReadWsResponse)

  fun observe(): Flow<ChatEvent>
}