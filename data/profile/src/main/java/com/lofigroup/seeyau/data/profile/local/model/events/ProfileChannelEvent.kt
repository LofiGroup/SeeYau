package com.lofigroup.seeyau.data.profile.local.model.events

import com.lofigroup.seeyau.data.profile.remote.http.model.BlackListDto

sealed interface ProfileChannelEvent {

  class NewBlacklist(val blacklist: List<BlackListDto>): ProfileChannelEvent

}