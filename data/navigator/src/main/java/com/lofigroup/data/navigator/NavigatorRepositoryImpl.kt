package com.lofigroup.data.navigator

import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.local.model.toDomainModel
import com.lofigroup.data.navigator.remote.NavigatorApi
import com.lofigroup.data.navigator.remote.model.toLocalDataModel
import com.lofigroup.data.navigator.remote.model.toUserEntity
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val userDao: UserDao,
  private val ioDispatcher: CoroutineDispatcher,
  private val api: NavigatorApi,
  private val ioScope: CoroutineScope
) : NavigatorRepository {

  override fun getNearbyUsers(): Flow<List<User>> =
    userDao.observeUsers().map { it.map { user -> user.toDomainModel() } }

  override suspend fun pullData() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getContacts())



      userDao.insert(response.map { it.toLocalDataModel() })
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun notifyUserWithIdWasFound(id: Long) = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.contactedWithUser(id))

      userDao.insert(response.toLocalDataModel())
      return@withContext
    }
    catch (e: HttpException) {
      Timber.e("Couldn't find user with id: $id. Error message ${e.message}")
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun getUser(id: Long) = withContext(ioDispatcher) {
    userDao.getUser(id)?.toDomainModel()
  }

}