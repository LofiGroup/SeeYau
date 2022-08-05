package com.lofigroup.data.navigator

import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.local.ProfileDataSource
import com.lofigroup.data.navigator.local.model.toUser
import com.lofigroup.data.navigator.local.model.toUserEntity
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(
  private val userData: ProfileDataSource,
  private val userDao: UserDao,
  private val ioDispatcher: CoroutineDispatcher
) : NavigatorRepository {

  override fun getNearbyUsers(): Flow<List<User>> =
    userDao.observeUsers().map { it.map { user -> user.toUser() } }

  override suspend fun saveUser(user: User) = withContext(ioDispatcher + NonCancellable) {
    userDao.upsert(user.toUserEntity())
  }

  override fun getMyProfile(): User {
    val profile = userData.getProfile()
    return User(
      id = profile.id,
      name = profile.name,
      imageUrl = "",
      isNear = false,
      lastConnection = 0,
      endpointId = ""
    )
  }

}