package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.LastChatUpdateDataSource
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.toDomainModel
import com.lofigroup.seeyau.data.chat.remote.ChatApi
import com.lofigroup.seeyau.data.chat.remote.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.models.toMessageEntity
import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.lofigroup.seeyau.domain.chat.ChatRepository
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
  private val chatApi: ChatApi,
  private val chatDao: ChatDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val lastChatUpdateDataSource: LastChatUpdateDataSource,
  private val profileDataSource: ProfileDataSource
): ChatRepository {

  override suspend fun pullData() = withContext(ioDispatcher) {
    val fromDate = lastChatUpdateDataSource.get()

    try {
      val updateTime = System.currentTimeMillis()

      val response = retrofitErrorHandler(chatApi.getChatUpdates(fromDate))
      for (chatUpdate in response)
        insertUpdates(chatUpdate)

      lastChatUpdateDataSource.update(updateTime)
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override fun getChats(): Flow<List<Chat>> {
    return chatDao.getChats().map { it.map { chat -> chat.toDomainModel() } }
  }

  private suspend fun insertUpdates(chatUpdates: ChatUpdatesDto) {
    val chat = ChatEntity(id = chatUpdates.id, partnerId = chatUpdates.partnerId)
    // TODO maybe not safe
    val messages = chatUpdates.newMessages.map { it.toMessageEntity(chat.id, myId = profileDataSource.getMyId()) }
    chatDao.insertChat(chat)
    chatDao.insertMessages(messages)
  }

}