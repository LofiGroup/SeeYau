package com.lofigroup.notifications

import android.app.Notification
import android.service.notification.StatusBarNotification
import com.lofigroup.notifications.model.NotificationChannelData

interface NotificationRequester {

  fun registerChannel(data: NotificationChannelData)

  fun showNotification(tag: String, notificationId: Int, notification: Notification)

  fun removeNotification(tag: String, notificationId: Int)

  fun getNotificationsByTag(tag: String): List<StatusBarNotification>

}