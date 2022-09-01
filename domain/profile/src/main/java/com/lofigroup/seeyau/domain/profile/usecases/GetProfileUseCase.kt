package com.lofigroup.seeyau.domain.profile.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.Profile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
  private val repository: ProfileRepository
) {

  operator fun invoke(): Flow<Profile> = repository.getProfile()

}