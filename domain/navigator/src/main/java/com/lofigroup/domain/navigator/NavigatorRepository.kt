package com.lofigroup.domain.navigator

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  fun getNearbyUsers(): Flow<List<User>>

  suspend fun saveUser(user: User)

  suspend fun getUser(id: Int): User?

  fun getMyProfile(): User

}