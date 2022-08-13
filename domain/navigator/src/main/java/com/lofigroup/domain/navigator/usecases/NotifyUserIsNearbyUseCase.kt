package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import javax.inject.Inject

class NotifyUserIsNearbyUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  suspend operator fun invoke(id: Long) {
    repository.notifyUserWithIdWasFound(id)
  }

}