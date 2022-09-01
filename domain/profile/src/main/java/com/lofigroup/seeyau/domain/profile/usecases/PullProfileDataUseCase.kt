package com.lofigroup.seeyau.domain.profile.usecases

import com.lofigroup.seeyau.domain.profile.ProfileRepository
import javax.inject.Inject

class PullProfileDataUseCase @Inject constructor(
  private val repository: ProfileRepository
) {

  suspend operator fun invoke() {
    repository.pullProfileData()
  }

}