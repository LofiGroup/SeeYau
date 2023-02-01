package com.lofigroup.seeyau.features.data_sync_service

import com.lofigroup.seeyau.common.chat.components.notifications.ChatNotificationBuilder
import javax.inject.Inject

class NotificationBuilder @Inject constructor(
  private val chatNotificationBuilder: ChatNotificationBuilder,

) {
}