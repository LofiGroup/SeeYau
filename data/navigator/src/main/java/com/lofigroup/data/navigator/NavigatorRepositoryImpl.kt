package com.lofigroup.data.navigator

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NavigatorRepositoryImpl @Inject constructor(

): NavigatorRepository {

  override fun getNearbyUsers(): Flow<Resource<List<User>>> = flow {
    emit(Resource.Loading())
    delay(2000L)
    emit(Resource.Success(
      data = listOf(
        User(id = 1, imageUrl = "", isNear = true, name = "User1", status = "Let's shine."),
        User(id = 2, imageUrl = "", isNear = true, name = "User2", status = "Let's shine."),
        User(id = 3, imageUrl = "", isNear = true, name = "User3", status = "Let's shine."),
        User(id = 4, imageUrl = "", isNear = true, name = "User4", status = "Let's shine."),
      )
    ))
  }

}