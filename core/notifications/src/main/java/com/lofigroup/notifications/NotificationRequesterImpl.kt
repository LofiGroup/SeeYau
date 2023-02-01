package com.lofigroup.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationManagerCompat
import com.lofigroup.notifications.model.NotificationChannelData

class NotificationRequesterImpl(
  private val context: Context
): NotificationRequester {

  private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }

  private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

  override fun registerChannel(data: NotificationChannelData) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(data.id, data.title, data.importance).apply {
        description = data.descriptionText
      }
      notificationManagerCompat.createNotificationChannel(channel)
    }
  }

  override fun showNotification(tag: String, notificationId: Int, notification: Notification) {
    notificationManagerCompat.notify(tag, notificationId, notification)
  }

  override fun removeNotification(tag: String, notificationId: Int) {
    notificationManagerCompat.cancel(tag, notificationId)
  }

  override fun getNotificationsByTag(tag: String): List<StatusBarNotification> {
    return if (Build.VERSION.SDK_INT >= 23) {
      notificationManager.activeNotifications.filter { it.tag == tag }
    } else emptyList()
  }

}