package com.lofigroup.seeyau.domain.base.user_notification_channel

import com.lofigroup.seeyau.domain.base.user_notification_channel.model.UserNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class UserNotificationChannel {
  private val events = MutableSharedFlow<UserNotification>(extraBufferCapacity = 1)

  fun sendNotification(userNotification: UserNotification) {
    events.tryEmit(userNotification)
  }

  fun observe(): Flow<UserNotification> = events

}