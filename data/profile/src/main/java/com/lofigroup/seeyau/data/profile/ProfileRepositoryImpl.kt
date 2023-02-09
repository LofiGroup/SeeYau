package com.lofigroup.seeyau.data.profile

import android.content.ContentResolver
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.data.profile.local.model.toLikeEntity
import com.lofigroup.seeyau.data.profile.local.model.toProfile
import com.lofigroup.seeyau.data.profile.remote.http.ProfileApi
import com.lofigroup.seeyau.data.profile.remote.http.model.toEntity
import com.lofigroup.seeyau.data.profile.remote.http.model.toUpdateProfileForm
import com.lofigroup.seeyau.data.profile.remote.http.model.toUserEntity
import com.lofigroup.seeyau.data.profile.remote.websocket.ProfileWebSocketListener
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.createMultipartBody
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val ioScope: CoroutineScope,
  private val profileData: ProfileDataSource,
  private val userDao: UserDao,
  private val likeDao: LikeDao,
  private val blacklistDao: BlacklistDao,
  private val contentResolver: ContentResolver,
  private val profileWebSocketListener: ProfileWebSocketListener,
  private val profileDataHandler: ProfileDataHandler
): ProfileRepository {

  override suspend fun pullProfileData() {
    safeIOCall(ioDispatcher) {
      val response = retrofitErrorHandler(api.getProfile())
      profileDataHandler.saveProfileData(response)
    }
  }

  override suspend fun pullContacts() {
    profileDataHandler.pullContacts()
  }

  override suspend fun pullUserData(userId: Long) {
    profileDataHandler.pullUserData(userId)
  }

  override suspend fun pullLikes() {
    safeIOCall(ioDispatcher) {
      val response = retrofitErrorHandler(api.getLikes(likeDao.getLastLikeUpdatedIn() ?: 0L))

      Timber.e(response.toString())
      likeDao.insertLikes(response.map { it.toLikeEntity(profileData.getMyId()) })
    }
  }

  override suspend fun pullBlacklist() {
    safeIOCall(ioDispatcher) {
      val fromDate = blacklistDao.getBlacklistLastCreatedIn() ?: 0L
      val response = retrofitErrorHandler(api.getBlackList(fromDate))
      Timber.e(response.toString())

      if (response.isEmpty()) return@safeIOCall

      profileDataHandler.handleBlacklistUpdates(response)
    }
  }

  override fun getProfile(): Flow<Profile> {
    return combine(
      userDao.observeMe(),
      likeDao.observeLikesCount()
    ) { profile, likesCount ->
      profile.toProfile(likesCount)
    }
  }

  override fun getUser(userId: Long): Flow<User> {
    return userDao.observeUser(userId).map { it.toDomainModel() }
  }

  override suspend fun getMyId(): Long {
    return profileData.getMyId()
  }

  override suspend fun updateProfile(profile: ProfileUpdate): Result = withContext(ioDispatcher) {
    return@withContext try {
      val newProfile = retrofitErrorHandler(
        api.updateProfile(
          form = profile.toUpdateProfileForm(),
          image = createMultipartBody("image", profile.imageUrl, contentResolver)
        )
      )

      profileDataHandler.saveProfileData(newProfile)
      Result.Success
    }
    catch (e: Exception) {
      Result.Error(getErrorMessage(e))
    }
  }

  override suspend fun getLastContactWith(userId: Long): Long? {
    return userDao.getLastContact(userId)
  }

  override suspend fun likeUser(userId: Long) {
    safeIOCall(ioDispatcher) {
      if (userId == 0L || userId == getMyId()) {
        return@safeIOCall
      }
      val response = retrofitErrorHandler(api.likeUser(userId))
      Timber.e(response.toString())
      likeDao.insert(response.toLikeEntity(profileData.getMyId()))
      pullUserData(userId)
    }
  }

  override suspend fun unlikeUser(userId: Long) = withContext(ioDispatcher) {
    try {
      if (userId == 0L || userId == getMyId()) {
        return@withContext
      }
      retrofitErrorHandler(api.unlikeUser(userId))
    }
    catch (e: EmptyResponseBodyException) {
      likeDao.unlike(userId)
      pullUserData(userId)
    }
    catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun blacklistUser(userId: Long) {
    safeIOCall(ioDispatcher) {
      userDao.delete(userId)

      val response = retrofitErrorHandler(api.blackListUser(userId))
      blacklistDao.insert(response.toEntity(getMyId()))
    }
  }
}