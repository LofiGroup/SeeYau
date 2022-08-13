package com.lofigroup.features.navigator_screen.model

import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core_time.Time

data class UserItemUIModel(
  val imageUrl: String?,
  val name: String,
  val isNear: Boolean
)

fun User.toUIModel(): UserItemUIModel {
  return UserItemUIModel(
    imageUrl = imageUrl,
    name = name,
    isNear = System.currentTimeMillis() - lastConnection < 1 * Time.m
  )
}