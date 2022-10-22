package com.lofigroup.seeyau.data.chat

import com.lofigroup.core.util.EventChannel
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.remote.http.ChatApi
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toChatEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.NewMessageWsResponse
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.events.ProfileChannelEvent
import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ChatDataHandler @Inject constructor(
  private val chatApi: ChatApi,
  private val chatDao: ChatDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val profileDataSource: ProfileDataSource,
  private val profileRepository: ProfileRepository,
  private val profileEventChannel: EventChannel<ProfileChannelEvent>,
  private val ioScope: CoroutineScope,
) {

  suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val fromDate = chatDao.getLastMessageCreatedIn() ?: 0L
      Timber.e("From date: $fromDate")

      val response = retrofitErrorHandler(chatApi.getChatUpdates(fromDate))
      Timber.e("Chat updates: $response")
      for (chatUpdate in response)
        insertUpdates(chatUpdate)
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  fun pullChatData(chatId: Long, fromDate: Long = 0L) {
    ioScope.launch(ioDispatcher) {
      try {
        val response = retrofitErrorHandler(chatApi.getChatData(chatId = chatId, fromDate = fromDate))
        profileRepository.pullUserData(response.partnerId)
        insertUpdates(response)
      }
      catch (e: Exception) {
        Timber.e(getErrorMessage(e))
      }
    }
  }

  fun saveMessage(webSocketResponse: NewMessageWsResponse) {
    ioScope.launch(ioDispatcher) {
      chatDao.insertOneMessages(webSocketResponse.messageDto, myId = profileDataSource.getMyId())
    }
  }

  private suspend fun insertUpdates(chatUpdates: ChatUpdatesDto) {
    val chat = chatUpdates.toChatEntity()
    val messages = chatUpdates.newMessages.map { it.toMessageEntity(myId = profileDataSource.getMyId(), readIn = chat.partnerLastVisited) }
    chatDao.upsertChat(chat)
    chatDao.insertMessages(messages)
  }
}