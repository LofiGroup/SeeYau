package com.lofigroup.features.navigator_screen.model

data class NavigatorScreenState(
  val users: List<UserItemUIModel> = emptyList(),
  val errorMessage: String? = null,
  val isLoading: Boolean = false
)