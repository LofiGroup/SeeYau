package com.lofigroup.seeyau.domain.profile.model

fun getUserPreviewModel() = User(
  id = 9,
  name = "York",
  imageUrl = "",
  isNear = true,
  lastContact = 0L,
  lastConnection = 0,
  isOnline = true,

  blacklistedYou = false,
  likedAt = null,
  likedYouAt = null
)