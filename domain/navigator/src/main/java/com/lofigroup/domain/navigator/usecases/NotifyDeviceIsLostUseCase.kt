package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import javax.inject.Inject

class NotifyDeviceIsLostUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  suspend operator fun invoke(id: Int) {
    val user = repository.getUser(id) ?: return

    repository.saveUser(user.copy(isNear = false))
  }

}