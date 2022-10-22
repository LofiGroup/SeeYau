package com.lofigroup.seeyau.domain.profile.usecases

import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
  private val repository: ProfileRepository
) {

  suspend operator fun invoke(profile: ProfileUpdate): Result {
    return repository.updateProfile(profile)
  }

}