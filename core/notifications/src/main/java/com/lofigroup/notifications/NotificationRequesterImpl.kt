package com.lofigroup.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.lofigroup.notifications.model.NotificationChannelData

class NotificationRequesterImpl(
  private val context: Context
): NotificationRequester {

  private val notificationManager by lazy { NotificationManagerCompat.from(context) }

  override fun registerChannel(data: NotificationChannelData) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(data.id, data.title, data.importance).apply {
        description = data.descriptionText
      }
      notificationManager.createNotificationChannel(channel)
    }
  }

  override fun showNotification(tag: String, notificationId: Int, notification: Notification) {
    notificationManager.notify(tag, notificationId, notification)
  }

  override fun removeNotification(tag: String, notificationId: Int) {
    notificationManager.cancel(tag, notificationId)
  }

}