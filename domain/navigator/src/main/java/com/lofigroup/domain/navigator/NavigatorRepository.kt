package com.lofigroup.domain.navigator

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  fun getNearbyUsers(): Flow<List<User>>

  fun getMyProfile(): Flow<Resource<User>>

  suspend fun notifyUserWithIdWasFound(id: Long)

  suspend fun getUser(id: Long): User?

}