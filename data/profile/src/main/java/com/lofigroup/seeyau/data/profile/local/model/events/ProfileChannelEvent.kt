package com.lofigroup.seeyau.data.profile.local.model.events

sealed interface ProfileChannelEvent {

  class UserIsLiked(val userId: Long): ProfileChannelEvent

  class UserIsUnliked(val userId: Long): ProfileChannelEvent

}