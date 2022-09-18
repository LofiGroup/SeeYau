package com.lofigroup.seeyau.data.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.core.util.getFileExtFromPath
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.remote.model.toUserEntity
import com.lofigroup.seeyau.data.profile.model.toProfile
import com.lofigroup.seeyau.data.profile.model.toUpdateProfileForm
import com.lofigroup.seeyau.data.profile.model.toUserDto
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.sillyapps.core_network.ContentUriRequestBody
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
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

      Timber.e("Got profile: $response")
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
      val newProfile = retrofitErrorHandler(
        api.updateProfile(
          form = profile.toUpdateProfileForm(),
          image = createMultipartBody(profile.imageUrl)
        )
      )

      userDao.insert(newProfile.toUserEntity())
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