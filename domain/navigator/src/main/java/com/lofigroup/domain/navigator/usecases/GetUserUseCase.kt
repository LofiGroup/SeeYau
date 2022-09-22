package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  operator fun invoke(id: Long): Flow<User> {
    return repository.getUser(id)
  }

}