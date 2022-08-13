package com.lofigroup.data.navigator.local

import com.lofigroup.core.util.Resource
import com.lofigroup.data.navigator.local.model.ProfileDataModel
import com.lofigroup.data.navigator.remote.model.UserDto
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {

  fun getProfile(): Flow<Resource<User>>

  fun update(profileRes: Resource<User>)

}