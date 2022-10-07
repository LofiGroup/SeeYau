package com.lofigroup.features.navigator_screen.model

data class NavigatorScreenState(
  val sortedUsers: List<UserItemUIModel> = emptyList(),
  val selectedUserPos: Int = NO_USER_SELECTED,
  val splitIndex: Int = 0,
  val errorMessage: String? = null,
  val fullScreenMode: Boolean = true,
  val newMessagesCount: Int = 0,
  val chatIsVisible: Boolean = false
) {
  val selectedUser: UserItemUIModel?
   get() {
     if (selectedUserPos == NO_USER_SELECTED) return null
     return sortedUsers.getOrNull(selectedUserPos)
   }

  companion object {
    const val NO_USER_SELECTED = -1
  }
}