package com.lofigroup.notifications

import android.app.Notification
import com.lofigroup.notifications.model.NotificationChannelData

interface NotificationRequester {

  fun registerChannel(data: NotificationChannelData)

  fun showNotification(tag: String, notificationId: Int, notification: Notification)

  fun removeNotification(tag: String, notificationId: Int)

}