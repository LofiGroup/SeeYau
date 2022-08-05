package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import javax.inject.Inject

class NotifyUserIsNearbyUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  suspend operator fun invoke(user: User) {
    repository.saveUser(user)
  }

}