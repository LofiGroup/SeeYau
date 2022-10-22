package com.lofigroup.data.navigator

import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.toUserEntity
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val userDao: UserDao,
  private val chatDao: ChatDao,
  private val likeDao: LikeDao,
  private val blacklistDao: BlacklistDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val api: NavigatorApi,
  private val ioScope: CoroutineScope,
  private val webSocketChannel: WebSocketChannel
) : NavigatorRepository {

  override suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getContacts())

      userDao.upsert(response.map { it.toUserEntity() })
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun notifyUserWithIdWasFound(id: Long) = withContext(ioDispatcher) {
    try {
      Timber.e("Found user with id: $id")
      if (blacklistDao.userIsInBlackList(id) != null) {
        Timber.e("User in blacklist")
        return@withContext
      }

      val response = retrofitErrorHandler(api.contactedWithUser(id))

      userDao.upsert(response.toUserEntity())
      return@withContext
    }
    catch (e: HttpException) {
      Timber.e("Couldn't find user with id: $id. Error message ${e.message}")
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override fun connectToWebsocket() {
    webSocketChannel.connect()
  }

  override fun getNearbyUsers(): Flow<List<NearbyUser>> {
    return userDao.observeUsers().flatMapLatest { users ->
      combine(users.map { user ->
        combine(
          chatDao.observeUserNewMessages(user.id),
          likeDao.observeLikedState(user.id)
        ) { newMessages, isLiked ->
          user.toNearbyUser(newMessages, isLiked ?: false)
        }
      }) { it.asList() }
    }
  }

}