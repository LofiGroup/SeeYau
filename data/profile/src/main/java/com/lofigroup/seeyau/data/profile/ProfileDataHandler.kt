package com.lofigroup.seeyau.data.profile

import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.core.util.splitInTwo
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.*
import com.lofigroup.seeyau.data.profile.remote.http.ProfileApi
import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto
import com.lofigroup.seeyau.data.profile.remote.http.model.ProfileDto
import com.lofigroup.seeyau.data.profile.remote.http.model.toEntity
import com.lofigroup.seeyau.data.profile.remote.http.model.toUserEntity
import com.lofigroup.seeyau.domain.profile.model.Like
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@AppScope
class ProfileDataHandler @Inject constructor(
  private val userDao: UserDao,
  private val likeDao: LikeDao,
  private val profileDataSource: ProfileDataSource,
  private val blacklistDao: BlacklistDao,
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val fileDownloader: FileDownloader,
  private val scope: CoroutineScope
) {

  suspend fun saveProfileData(profileDto: ProfileDto) {
    safeIOCall(ioDispatcher) {
      saveUserImage(0, profileDto.imageUrl)

      userDao.upsert(profileDto.toUserEntity())
      profileDataSource.update(profileDto.id)
    }
  }

  suspend fun pullUserData(userId: Long) {
    safeIOCall(ioDispatcher) {
      if (userId == 0L || userId == getMyId()) return@safeIOCall

      val response = retrofitErrorHandler(api.getUser(userId))
      saveUserImage(response.id, response.imageUrl)

      userDao.upsert(response.toUserEntity())
    }
  }

  suspend fun pullContacts() {
    safeIOCall(ioDispatcher) {
      val response = retrofitErrorHandler(api.getContacts())

      for (user in response) {
        saveUserImage(user.id, user.imageUrl)
      }
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
    return userDao.observeAssembledUsers()
  }

  fun observeUserLike(userId: Long): Flow<Like?> {
    return likeDao.observeUserLike(userId).map { it?.toDomainModel() }
  }

  // Returning null if user is already in the database
  suspend fun insertUser(user: UserDto): UserEntity? {
    val uri = getUserImage(user)

    val userEntity = user.toUserEntity(imageContentUri = uri)
    val userExists = userDao.upsert(userEntity)

    return if (!userExists) userEntity else null
  }

  suspend fun handleBlacklistUpdates(blacklistUpdates: List<BlackListDto>) {
    val (toInsert, toDelete) = blacklistUpdates.splitInTwo { it.isActive }

    insertBlacklist(toInsert)
    removeBlacklist(toDelete)
  }

  suspend fun handleWhenYouAreBlacklisted(blacklist: BlackListDto) {
    Timber.e("Handling blacklist: $blacklist")
    if (blacklist.toWhom != 0L) return

    if (blacklist.isActive) {
      userDao.delete(blacklist.byWho)
      blacklistDao.insert(blacklist.toEntity(getMyId()))
    }
    else {
      blacklistDao.delete(blacklist.toEntity(getMyId()))
    }
  }

  private suspend fun insertBlacklist(toInsert: List<BlackListDto>) {
    val (blackListed, inBlackList) = toInsert.splitInTwo { it.byWho == getMyId() }

    val blacklistedUserIds = blackListed.map { it.toWhom } + inBlackList.map { it.byWho }
    userDao.deleteMultiple(blacklistedUserIds)

    blacklistDao.insert(toInsert.map { it.toEntity(getMyId()) })
  }

  private suspend fun removeBlacklist(toDelete: List<BlackListDto>) {
    blacklistDao.delete(toDelete.map { it.toEntity(getMyId()) })
  }

  private fun saveUserImage(userId: Long, imageUrl: String?) {
    if (imageUrl == null) return
    scope.launch(Dispatchers.IO) {
      val uri = downloadUserImage(userId, imageUrl) ?: return@launch

      userDao.updateImageUrl(
        UpdateUserImage(
          id = userId,
          imageContentUri = uri,
          imageRemoteUrl = imageUrl
        )
      )
    }
  }

  private suspend fun getUserImage(userDto: UserDto): String? = withContext(Dispatchers.IO) {
    downloadUserImage(userDto.id, userDto.imageUrl)
  }

  private suspend fun downloadUserImage(userId: Long, imageUrl: String?): String? {
    if (imageUrl == null) return null

    val localUser = userDao.getUser(userId)

    return if (localUser == null || localUser.imageRemoteUrl != imageUrl) {
      fileDownloader.downloadToInternalStorage(imageUrl)
    } else localUser.imageContentUri
  }
}