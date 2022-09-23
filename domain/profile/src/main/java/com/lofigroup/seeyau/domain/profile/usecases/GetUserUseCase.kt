package com.lofigroup.seeyau.domain.profile.usecases

import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: ProfileRepository
) {

  operator fun invoke(id: Long): Flow<User> {
    return repository.getUser(id)
  }

}