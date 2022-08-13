package com.lofigroup.data.navigator.local

import android.content.SharedPreferences
import com.lofigroup.core.util.Resource
import com.lofigroup.data.navigator.local.model.ProfileDataModel
import com.lofigroup.data.navigator.remote.model.UserDto
import com.lofigroup.domain.navigator.model.User
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

class ProfileDataSourceImpl @Inject constructor(
): ProfileDataSource {

  private var mProfile: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Loading())

  override fun getProfile(): Flow<Resource<User>> {
    return mProfile
  }

  override fun update(profileRes: Resource<User>) {
    mProfile.value = profileRes
  }
}