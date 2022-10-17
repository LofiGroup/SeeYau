package com.lofigroup.data.navigator

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.toUserEntity
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val userDao: UserDao,
  private val chatDao: ChatDao,
  private val likeDao: LikeDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val api: NavigatorApi,
  private val ioScope: CoroutineScope
) : NavigatorRepository {

  override suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getContacts())

      userDao.insert(response.map { it.toUserEntity() })
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun notifyUserWithIdWasFound(id: Long) = withContext(ioDispatcher) {
    try {
      Timber.e("Found user with id: $id")
      val response = retrofitErrorHandler(api.contactedWithUser(id))

      userDao.insert(response.toUserEntity())
      return@withContext
    }
    catch (e: HttpException) {
      Timber.e("Couldn't find user with id: $id. Error message ${e.message}")
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
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