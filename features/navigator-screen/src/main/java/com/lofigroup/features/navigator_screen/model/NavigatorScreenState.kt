package com.lofigroup.features.navigator_screen.model

data class NavigatorScreenState(
  val sortedUsers: List<UserItemUIModel> = emptyList(),
  val splitIndex: Int = 0,
  val errorMessage: String? = null,
  val selectedUser: UserItemUIModel? = null,
  val fullScreenMode: Boolean = true,
  val newMessagesCount: Int = 0,
  val chatIsVisible: Boolean = false
)