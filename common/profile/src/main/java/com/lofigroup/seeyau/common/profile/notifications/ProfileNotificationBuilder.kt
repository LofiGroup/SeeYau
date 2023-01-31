package com.lofigroup.seeyau.common.profile.notifications

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.model.NotificationChannelData
import com.lofigroup.seeyau.common.profile.R
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.domain.chat.usecases.GetChatIdByUserIdUseCase
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core.ui.util.extractBitmapFromUri
import com.sillyapps.core.ui.util.getCompatPendingIntentFlags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lofigroup.seeyau.common.ui.R as CommonR

class ProfileNotificationBuilder @Inject constructor(
  private val context: Context,
  private val notificationRequester: NotificationRequester,
  private val mainScreenEventChannel: MainScreenEventChannel
) {

  init {
    val notificationChannelData = NotificationChannelData(
      title = context.getString(R.string.profile_notification_channel_title),
      descriptionText = context.getString(R.string.profile_notification_channel_text),
      id = CHANNEL_ID,
      importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )
    notificationRequester.registerChannel(data = notificationChannelData)
  }

  private val baseNotificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(CommonR.drawable.ic_app_icon)
    .setAutoCancel(true)
    .setContentTitle(context.getString(R.string.found_new_user))
    .setContentText(context.getString(R.string.new_user_is_somewhere_nearby))
    .setGroup(GROUP_ID)
    .setGroupSummary(false)
    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)

  private val groupSummary = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(CommonR.drawable.ic_app_icon)
    .setAutoCancel(true)
    .setGroupSummary(true)
    .setGroup(GROUP_ID)
    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)


  fun sendNotification(user: User, chatId: Long?) {
    val notification = baseNotificationBuilder
      .setLargeIcon(extractBitmapFromUri(context, user.imageUrl))
      .setContentIntent(resolveContentIntent(chatId))
      .build()

    notificationRequester.showNotification(NOTIFICATION_TAG, user.id.toInt(), notification)

    notificationRequester.showNotification(NOTIFICATION_TAG, SUMMARY_NOTIFICATION_ID, groupSummary.build())
  }

  private fun resolveContentIntent(chatId: Long?): PendingIntent? {
    if (chatId == null) return null

    val intent = mainScreenEventChannel.getEventIntent(MainScreenEvent.OpenChat(chatId))

    return PendingIntent.getActivity(context, 0, intent, getCompatPendingIntentFlags())
  }

  companion object {
    private const val CHANNEL_ID = "users_nearby"
    private const val GROUP_ID = "nearby_users"

    private const val NOTIFICATION_TAG = "NEARBY_USERS"
    private const val SUMMARY_NOTIFICATION_ID = 555555555
  }

}