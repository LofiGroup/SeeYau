package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import javax.inject.Inject

class NotifyDeviceIsLostUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  suspend operator fun invoke(deviceId: String) {
    val user = repository.getUserWithDeviceId(deviceId) ?: return

    repository.saveUser(user.copy(isNear = false))
  }

}