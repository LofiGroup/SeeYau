package com.lofigroup.seeyau.data.profile

import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.UserAssembled
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.data.profile.local.model.toUserEntity
import com.lofigroup.seeyau.data.profile.remote.http.ProfileApi
import com.lofigroup.seeyau.domain.profile.model.Like
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ProfileDataHandler @Inject constructor(
  private val userDao: UserDao,
  private val likeDao: LikeDao,
  private val profileDataSource: ProfileDataSource,
  private val blacklistDao: BlacklistDao,
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher
) {

  suspend fun pullUserData(userId: Long) {
    safeIOCall(ioDispatcher) {
      if (userId == 0L || userId == getMyId()) {
        return@safeIOCall
      }
      val response = retrofitErrorHandler(api.getUser(userId))

      userDao.upsert(response.toUserEntity())
    }
  }

  suspend fun pullContacts() {
    safeIOCall(ioDispatcher) {
      val response = retrofitErrorHandler(api.getContacts())
      userDao.upsert(response.map { it.toUserEntity() })
    }
  }

  suspend fun userIsInBlackList(userId: Long): Boolean {
    return blacklistDao.userIsInBlackList(userId)
  }

  fun getMyId(): Long {
    return profileDataSource.getMyId()
  }

  fun observeAssembledUser(userId: Long): Flow<UserAssembled> {
    return userDao.observeAssembledUser(userId)
  }

  fun observeAssembledUsers(): Flow<List<UserAssembled>> {
    return userDao.observeAssembledUsers().map { it.filter { user -> user.blacklistedYouAt == null } }
  }

  fun observeUserLike(userId: Long): Flow<Like?> {
    return likeDao.observeUserLike(userId).map { it?.toDomainModel() }
  }

  suspend fun insertUser(user: UserEntity) {
    userDao.upsert(user)
  }
}