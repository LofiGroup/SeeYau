package com.lofigroup.seeyau.features.chat.model

import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.domain.profile.model.User

data class ChatScreenState(
  val partner: User = User(id = -1, name = "", imageUrl = null, lastConnection = 0L, isNear = false, isOnline = false, lastContact = 0L),
  val messages: Map<String, List<UIChatMessage>> = emptyMap(),
  val message: String = "",
  val currentCloseUpImage: String? = null,
  val currentCloseUpVideo: UIMessageType.Video = UIMessageType.Video(mediaItem = MediaItem.EMPTY, aspectRatio = 1f, duration = 0L),
)
