package com.lofigroup.seeyau.data.profile

import android.content.ContentResolver
import android.net.Uri
import com.lofigroup.core.util.Result
import com.lofigroup.core.util.getFileExtFromPath
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.data.profile.local.model.toProfile
import com.lofigroup.seeyau.data.profile.local.model.toUserEntity
import com.lofigroup.seeyau.data.profile.remote.ProfileApi
import com.lofigroup.seeyau.data.profile.remote.model.toUpdateProfileForm
import com.lofigroup.seeyau.data.profile.remote.model.toUserEntity
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core_network.ContentUriRequestBody
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val ioScope: CoroutineScope,
  private val profileData: ProfileDataSource,
  private val userDao: UserDao,
  private val contentResolver: ContentResolver
): ProfileRepository {

  override suspend fun pullProfileData() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getProfile())

      userDao.insert(response.toUserEntity())
      profileData.update(response.id)
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override suspend fun pullUserData(userId: Long) = withContext(ioDispatcher) {
    try {
      if (userId == 0L) {
        Timber.e("Wrong id")
        return@withContext
      }
      val response = retrofitErrorHandler(api.getUser(userId))

      userDao.insert(response.toUserEntity())
      Unit
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override fun getProfile(): Flow<Profile> {
    return userDao.observeMe().map {
      it.toProfile()
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
          image = createMultipartBody(profile.imageUrl)
        )
      )

      userDao.insert(newProfile.toUserEntity())
      profileData.update(newProfile.id)
      Result.Success
    }
    catch (e: Exception) {
      Result.Error(getErrorMessage(e))
    }
  }

  override suspend fun getLastContactWith(userId: Long): Long? {
    return userDao.getLastContact(userId)
  }

  private fun createMultipartBody(uri: String?): MultipartBody.Part? {
    val uri = uri ?: return null

    val imageUri = Uri.parse(uri)
    if (imageUri.scheme != "content") return null

    val fileExt = getFileExtFromPath(uri)

    return MultipartBody.Part.createFormData(
      name = "image",
      filename = "image.$fileExt",
      body = ContentUriRequestBody(contentResolver, imageUri)
    )
  }
}