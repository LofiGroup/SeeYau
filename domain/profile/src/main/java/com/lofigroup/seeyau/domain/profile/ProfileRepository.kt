package com.lofigroup.seeyau.domain.profile

import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.profile.model.Profile
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.lofigroup.seeyau.domain.profile.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  suspend fun pullProfileData()
  suspend fun pullContacts()
  suspend fun pullUserData(userId: Long)
  suspend fun pullLikes()
  suspend fun pullBlacklist()

  fun getProfile(): Flow<Profile>

  fun getUser(userId: Long): Flow<User>

  suspend fun getMyId(): Long

  suspend fun updateProfile(profile: ProfileUpdate): Result

  suspend fun getLastContactWith(userId: Long): Long?

  suspend fun likeUser(userId: Long)

  suspend fun unlikeUser(userId: Long)

  suspend fun blacklistUser(userId: Long)

}