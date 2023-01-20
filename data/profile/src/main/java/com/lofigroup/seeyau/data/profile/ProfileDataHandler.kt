package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.splitInTwo
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.*
import com.lofigroup.seeyau.data.profile.remote.http.ProfileApi
import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto
import com.lofigroup.seeyau.data.profile.remote.http.model.toEntity
import com.lofigroup.seeyau.domain.profile.model.Like
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

  suspend fun pullUserData(userId: Long) {
    safeIOCall(ioDispatcher) {
      if (userId == 0L || userId == getMyId()) return@safeIOCall

      val response = retrofitErrorHandler(api.getUser(userId))
      downloadUserPicture(userId, response.imageUrl)

      userDao.upsert(response.toUserEntity())
    }
  }

  suspend fun pullContacts() {
    safeIOCall(ioDispatcher) {
      val response = retrofitErrorHandler(api.getContacts())

      for (user in response) {
        downloadUserPicture(user.id, user.imageUrl)
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

  suspend fun insertUser(user: UserEntity) {
    userDao.upsert(user)
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

  private fun downloadUserPicture(userId: Long, imageUrl: String?) {
    if (imageUrl == null) return

    scope.launch(Dispatchers.IO) {
      val localUser = userDao.getUser(userId)

      if (localUser == null || localUser.imageRemoteUrl != imageUrl) {
        Timber.e("Starting download for user with id: $userId, imageUri: $imageUrl")
        val uri = fileDownloader.downloadToInternalStorage(imageUrl) ?: return@launch

        try {
          userDao.updateImageUrl(
            UpdateUserImage(
              id = userId,
              imageContentUri = uri,
              imageRemoteUrl = imageUrl
            )
          )
          Timber.e("Download is completed for user with id: $userId, new imageUri: $uri")
        } catch (e: Exception) {
          Timber.e(e)
        }
      }
    }

  }
}