package com.lofigroup.notifications.model

import android.app.NotificationManager

data class NotificationChannelData(
  val title: String,
  val descriptionText: String,
  val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
  val id: String,
)