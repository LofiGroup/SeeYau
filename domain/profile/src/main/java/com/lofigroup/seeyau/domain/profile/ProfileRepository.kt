package com.lofigroup.seeyau.domain.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.profile.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  fun getProfile(): Flow<Resource<Profile>>

  suspend fun updateProfile(profile: Profile): Result

}