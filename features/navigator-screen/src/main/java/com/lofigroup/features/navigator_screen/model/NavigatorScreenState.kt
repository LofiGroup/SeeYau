package com.lofigroup.features.navigator_screen.model

import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core_time.Time

data class NavigatorScreenState(
  val nearbyUsers: List<UserItemUIModel> = emptyList(),
  val metUsers: List<UserItemUIModel> = emptyList(),
  val errorMessage: String? = null,
  val selectedUser: UserItemUIModel? = null,
  val fullScreenMode: Boolean = true,
  val newMessagesCount: Int = 0,
  val chatIsVisible: Boolean = false
)