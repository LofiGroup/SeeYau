package com.lofigroup.seeyau.data.profile

import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.remote.http.ProfileApi
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileDataHandler @Inject constructor(
  private val userDao: UserDao,
  private val likeDao: LikeDao,
  private val api: ProfileApi,
  private val ioDispatcher: CoroutineDispatcher
) {



}