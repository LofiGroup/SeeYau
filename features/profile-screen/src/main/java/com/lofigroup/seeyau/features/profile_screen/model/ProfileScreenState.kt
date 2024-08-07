package com.lofigroup.seeyau.features.profile_screen.model

import com.lofigroup.seeyau.domain.profile.model.Profile
import com.lofigroup.seeyau.domain.profile.model.ProfileUpdate
import com.lofigroup.seeyau.domain.settings.model.Visibility

data class ProfileScreenState(
  val id: Long = 0L,
  val errorMessage: String? = null,
  val name: String = "",
  val imageUrl: String = "",
  val likesCount: Int = 0,

  val isVisible: Boolean = true,
)

fun ProfileScreenState.toProfileUpdate() = ProfileUpdate(
  name = name,
  imageUrl = imageUrl
)

fun ProfileScreenState.applyProfileUpdates(profile: Profile) = copy(
  id = profile.id,
  name = profile.name,
  imageUrl = profile.imageUrl ?: "",
  likesCount = profile.likesCount
)

fun ProfileScreenState.applyVisibilityUpdates(visibility: Visibility) = copy(
  isVisible = visibility.isVisible
)