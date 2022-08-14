package com.lofigroup.data.navigator

import com.lofigroup.core.util.Resource
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.local.model.toUser
import com.lofigroup.data.navigator.remote.NavigatorApi
import com.lofigroup.data.navigator.remote.model.toUserEntity
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val userDao: UserDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val api: NavigatorApi,
  private val ioScope: CoroutineScope
) : NavigatorRepository {

  override fun getNearbyUsers(): Flow<List<User>> =
    userDao.observeUsers().map { it.map { user -> user.toUser() } }

  override suspend fun notifyUserWithIdWasFound(id: Long) = withContext(ioDispatcher) {
    val cached = userDao.getUser(id)
    if (cached != null) {
      userDao.update(cached.copy(lastConnection = System.currentTimeMillis()))
      return@withContext
    }

    try {
      val response = retrofitErrorHandler(api.getUser(id))

      userDao.upsert(response.toUserEntity())
    } catch (e: Exception) {
      Timber.e("Couldn't find user with id: $id. Error message ${e.message}")
    }
  }

  override suspend fun getUser(id: Long) = withContext(ioDispatcher) {
    userDao.getUser(id)?.toUser()
  }

}