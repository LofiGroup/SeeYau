package com.lofigroup.features.nearby_service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.features.nearby_service.R
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.model.NotificationChannelData
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.sillyapps.core.ui.util.getCompatPendingIntentFlags
import javax.inject.Inject
import com.lofigroup.seeyau.common.ui.R as CommonR

class NearbyServiceNotificationBuilder @Inject constructor(
  private val context: Context,
  private val notificationRequester: NotificationRequester,
  private val mainScreenEventChannel: MainScreenEventChannel
) {

  init {
    val notificationChannelData = NotificationChannelData(
      title = context.getString(R.string.nearby_service_notification_channel_title),
      descriptionText = context.getString(R.string.nearby_service_notification_channel_description),
      id = CHANNEL_ID,
      importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )
    notificationRequester.registerChannel(data = notificationChannelData)
  }

  private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setSmallIcon(CommonR.drawable.ic_app_icon)
    .setContentTitle(context.getString(R.string.ploom))
    .setContentText(context.getString(R.string.searching_for_people))
    .addAction(
      R.drawable.ic_baseline_stop_24,
      context.getString(R.string.stop),
      getPendingIntent(NearbyServiceImpl.ACTION_STOP)
    )
    .setContentIntent(
      PendingIntent.getActivity(
        context, 0, mainScreenEventChannel.getEventIntent(MainScreenEvent.OpenApp()), getCompatPendingIntentFlags()
      ),
    )
    .setAutoCancel(false)
    .setOngoing(true)
    .setOnlyAlertOnce(true)


  fun getNotification(): Notification {
    return notificationBuilder.build()
  }

  private fun getPendingIntent(action: String): PendingIntent {
    val intent = Intent(context, NearbyServiceImpl::class.java)
    intent.action = action
    return PendingIntent.getService(context, 0, intent, getCompatPendingIntentFlags())
  }

  companion object {
    private const val CHANNEL_ID = "NearbyServiceChannelId"
  }

}