package com.lofigroup.seeyau.data.chat.local

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.ChatIsReadWsResponse
import com.lofigroup.seeyau.domain.chat.models.events.ChatEvent
import com.lofigroup.seeyau.domain.chat.models.events.ChatIsRead
import com.lofigroup.seeyau.domain.chat.models.events.NewChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class EventsDataSource @Inject constructor() {
  private val events = MutableSharedFlow<ChatEvent>()
  private var mChatId: Long = -1

  fun onNewMessageEvent(message: ChatMessageDto) {
    if (mChatId == message.chatId)
      events.tryEmit(NewChatMessage(message.author == 0L))
  }

  fun onChatIsReadEvent(response: ChatIsReadWsResponse) {
    if (mChatId == response.chatId)
      events.tryEmit(ChatIsRead)
  }

  fun observe(chatId: Long): Flow<ChatEvent> {
    mChatId = chatId
    return events
  }

}