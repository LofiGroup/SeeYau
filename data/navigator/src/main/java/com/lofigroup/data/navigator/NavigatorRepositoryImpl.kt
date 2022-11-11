package com.lofigroup.data.navigator

import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.core.util.timerFlow
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.data.chat.ChatDataHandler
import com.lofigroup.seeyau.data.profile.ProfileDataHandler
import com.lofigroup.seeyau.data.profile.local.model.toUserEntity
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_time.Time
import com.sillyapps.core_time.shouldUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val ioDispatcher: CoroutineDispatcher,
  private val api: NavigatorApi,
  private val ioScope: CoroutineScope,
  private val webSocketChannel: WebSocketChannel,
  private val profileDataHandler: ProfileDataHandler,
  private val chatDataHandler: ChatDataHandler
) : NavigatorRepository {

  private var lastCallToApi = HashMap<Long, Long>()
  private val callInterval = 4 * Time.s

  override suspend fun notifyUserWithIdWasFound(id: Long) = withContext(ioDispatcher) {
    try {
      Timber.e("Found user with id: $id")
      if (profileDataHandler.userIsInBlackList(id)) {
        Timber.e("User in blacklist")
        return@withContext
      }

      if (!shouldUpdate(lastCallToApi[id], callInterval)) return@withContext
      lastCallToApi[id] = System.currentTimeMillis()

      val response = retrofitErrorHandler(api.contactedWithUser(id))

      profileDataHandler.insertUser(response.toUserEntity())
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
    return combine(
      nearbyUsersFlow(),
      timerFlow(30 * Time.s)
    ) { users, update ->
      users
    }
  }

  private fun nearbyUsersFlow() = profileDataHandler.observeAssembledUsers().flatMapLatest { users ->
    combine(users.map { user ->
      chatDataHandler.observeUserNewMessages(user.id).map { newMessages ->
        user.toNearbyUser(newMessages)
      }
    }
    ) { it.asList() }
  }

}