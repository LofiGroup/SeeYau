package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.data.profile.model.ProfileDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(

): ProfileDataSource {
  private var mProfile: MutableStateFlow<Resource<ProfileDataModel>> = MutableStateFlow(Resource.Loading())

  override fun getProfile(): Flow<Resource<ProfileDataModel>> {
    return mProfile
  }

  override fun update(profileRes: Resource<ProfileDataModel>) {
    mProfile.value = profileRes
  }

  override fun isNotEmpty(): Boolean {
    return mProfile.value is Resource.Success
  }
}