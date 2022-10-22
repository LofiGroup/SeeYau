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
  private var mChatId: Long = -1

  init {
    Timber.e("New event data source is created!")
  }

  override fun onNewMessageEvent(message: ChatMessageDto) {
    if (mChatId == message.chatId)
      events.tryEmit(NewChatMessage(message.author == 0L))
  }

  override fun onChatIsReadEvent(response: ChatIsReadWsResponse) {
    if (mChatId == response.chatId)
      events.tryEmit(ChatIsRead)
  }

  override fun observe(chatId: Long): Flow<ChatEvent> {
    mChatId = chatId
    return events
  }

}