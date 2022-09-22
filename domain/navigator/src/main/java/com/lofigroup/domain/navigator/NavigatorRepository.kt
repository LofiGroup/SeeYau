package com.lofigroup.domain.navigator

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  fun getNearbyUsers(): Flow<List<User>>

  suspend fun pullData()

  suspend fun notifyUserWithIdWasFound(id: Long)

  fun getUser(id: Long): Flow<User>

}