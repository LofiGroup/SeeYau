package com.lofigroup.seeyau.data.chat.local

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject

class EventsDataSourceImpl @Inject constructor(): EventsDataSource {
  private val events = MutableSharedFlow<ChatEvent>(extraBufferCapacity = 1)

  override fun onNewMessageEvent(event: NewChatMessage) {
    events.tryEmit(event)
  }

  override fun onChatIsReadEvent(response: ChatIsReadWsResponse) {
    events.tryEmit(ChatIsRead(response.chatId))
  }

  override fun observe(): Flow<ChatEvent> {
    return events
  }

}