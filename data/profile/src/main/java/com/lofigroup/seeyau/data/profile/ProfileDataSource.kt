package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.data.profile.model.ProfileDataModel
import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {

  fun getProfile(): Flow<Resource<ProfileDataModel>>

  fun update(profileRes: Resource<ProfileDataModel>)

  fun isNotEmpty(): Boolean

}