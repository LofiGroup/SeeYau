package com.lofigroup.seeyau.features.profile_screen.model

import com.lofigroup.seeyau.domain.profile.model.Profile

data class ProfileScreenState(
  val id: Long = 0L,
  val errorMessage: String? = null,
  val name: String = "",
  val imageUrl: String = "",

  val navigateOut: Boolean = false,
  val isLoading: Boolean = true
)

fun ProfileScreenState.toProfile() = Profile(
  id = id,
  name = name,
  imageUrl = imageUrl
)

fun Profile.toProfileScreenState() = ProfileScreenState(
  id = id,
  name = name,
  imageUrl = imageUrl ?: "",
  isLoading = false
)