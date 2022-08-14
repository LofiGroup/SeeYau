package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.data.profile.model.toProfile
import com.lofigroup.seeyau.data.profile.model.toProfileDataModel
import com.lofigroup.seeyau.data.profile.model.toUpdateProfileRequest
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val ioScope: CoroutineScope,
  private val profileData: ProfileDataSource
): ProfileRepository {

  private suspend fun loadProfile() = withContext(ioDispatcher) {
    try {
      val response = retrofitErrorHandler(api.getProfile())

      profileData.update(Resource.Success(response.toProfileDataModel()))
    } catch (e: HttpException) {
      profileData.update(Resource.Error(e.message ?: "Unknown error"))
    }
  }

  override fun getProfile(): Flow<Resource<Profile>> {
    if (!profileData.isNotEmpty()) {
      ioScope.launch(ioDispatcher) {
        loadProfile()
      }
    }

    return profileData.getProfile().map {
      when (it) {
        is Resource.Loading -> Resource.Loading()
        is Resource.Success -> Resource.Success(it.data.toProfile())
        is Resource.Error -> Resource.Error(it.errorMessage)
      }
    }
  }

  override suspend fun updateProfile(profile: Profile): Result = withContext(ioDispatcher) {
    return@withContext try {
      retrofitErrorHandler(api.updateProfile(profile.toUpdateProfileRequest()))

      profileData.update(Resource.Success(profile.toProfileDataModel()))
      Result.Success
    }
    catch (e: EmptyResponseBodyException) {
      profileData.update(Resource.Success(profile.toProfileDataModel()))
      Result.Success
    }
    catch (e: HttpException) {
      Result.Error(e.message ?: " Unknown error")
    }
  }
}