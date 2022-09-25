package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import javax.inject.Inject

class NotifyUserIsNearbyUseCase @Inject constructor(
  private val repository: NavigatorRepository,
  private val profileRepository: ProfileRepository
) {

  suspend operator fun invoke(id: Long) {
    if (shouldUpdate(id)) {
      repository.notifyUserWithIdWasFound(id)
    }
  }

  private suspend fun shouldUpdate(id: Long): Boolean {
    val lastContact = profileRepository.getLastContactWith(id) ?: return true
    return (System.currentTimeMillis() - lastContact > TIME_BETWEEN_UPDATES)
  }

  companion object {
    const val TIME_BETWEEN_UPDATES = 60000L
  }

}