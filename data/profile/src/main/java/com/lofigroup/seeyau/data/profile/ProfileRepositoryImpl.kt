package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.remote.model.toUserEntity
import com.lofigroup.seeyau.data.profile.model.toProfile
import com.lofigroup.seeyau.data.profile.model.toUpdateProfileRequest
import com.lofigroup.seeyau.data.profile.model.toUserDto
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val ioScope: CoroutineScope,
  private val profileData: ProfileDataSource,
  private val userDao: UserDao
): ProfileRepository {

  override suspend fun pullProfileData() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getProfile())

      userDao.insert(response.toUserEntity().copy(id = 0))
      profileData.update(response.id)
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }

  override fun getProfile(): Flow<Profile> {
    return userDao.observeMe().map {
      it.toProfile()
    }
  }

  override suspend fun getMyId(): Long {
    return profileData.getMyId()
  }

  override suspend fun updateProfile(profile: Profile): Result = withContext(ioDispatcher) {
    return@withContext try {
      retrofitErrorHandler(api.updateProfile(profile.toUpdateProfileRequest()))

      userDao.insert(profile.toUserDto().toUserEntity())
      Result.Success
    }
    catch (e: EmptyResponseBodyException) {
      userDao.insert(profile.toUserDto().toUserEntity())
      Result.Success
    }
    catch (e: Exception) {
      Result.Error(getErrorMessage(e))
    }
  }
}