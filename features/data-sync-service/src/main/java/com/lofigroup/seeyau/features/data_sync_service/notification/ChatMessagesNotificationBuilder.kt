package com.lofigroup.seeyau.features.data_sync_service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.model.NotificationChannelData
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.domain.chat.models.ChatNewMessages
import com.lofigroup.seeyau.features.data_sync_service.R
import com.lofigroup.seeyau.features.data_sync_service.data.getBitmapFromContentUri
import com.lofigroup.seeyau.features.data_sync_service.data.toPerson
import com.sillyapps.core.ui.util.getCompatPendingIntentFlags
import timber.log.Timber
import com.lofigroup.seeyau.common.ui.R as CommonR
import javax.inject.Inject

class ChatMessagesNotificationBuilder @Inject constructor(
  private val context: Context,
  private val notificationRequester: NotificationRequester,
  private val mainScreenEventChannel: MainScreenEventChannel
) {
  init {
    val notificationChannelData = NotificationChannelData(
      title = context.getString(R.string.chat_messages_notification_channel_title),
      descriptionText = context.getString(R.string.chat_messages_notification_channel_description),
      id = CHANNEL_ID,
      importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )
    notificationRequester.registerChannel(data = notificationChannelData)
  }

  fun sendNotification(newMessages: List<ChatNewMessages>) {
    Timber.e("New messages: $newMessages")

    val notifications = mutableMapOf<Int, Notification>()

    for (messages in newMessages) {
      val avatar = getBitmapFromContentUri(context, messages.partner.imageUrl)
      val person = messages.partner.toPerson(context, avatar)

      registerConversationShortcut(person, messages.chatId)

      val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(CommonR.drawable.ic_interaction_notification_icon)
        .setAutoCancel(true)
        .setContentIntent(PendingIntent.getActivity(
          context,
          0,
          mainScreenEventChannel.getEventIntent(MainScreenEvent.OpenChat(chatId = messages.chatId)),
          getCompatPendingIntentFlags()
        ))
        .setGroup(GROUP_ID)
        .setGroupSummary(false)
        .setWhen(messages.chatMessages[0].createdIn)
        .setShowWhen(true)
        .setNumber(messages.count)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
        .setShortcutId(messages.chatId.toString())


      val style = NotificationCompat.MessagingStyle(person)
        .setGroupConversation(false)

      for (i in messages.chatMessages.size-1 downTo 0) {
        val message = messages.chatMessages[i]
        style.addMessage(NotificationCompat.MessagingStyle.Message(
          message.message,
          message.createdIn,
          person
        ))
      }

      style.setBuilder(notificationBuilder)

      // TODO change notification id from server given id to local
      notifications[messages.chatId.toInt()] = notificationBuilder.build()
    }

    val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(CommonR.drawable.ic_app_icon)
      .setStyle(NotificationCompat.InboxStyle()
        .setSummaryText(context.getString(R.string.new_messages_count, newMessages.sumOf { it.count })))
      .setGroup(GROUP_ID)
      .setGroupSummary(true)
      .build()

    notificationRequester.showNotification(TAG, SUMMARY_ID, summaryNotification)

    notifications.forEach {
      notificationRequester.showNotification(TAG, it.key, it.value)
    }
  }

  private fun registerConversationShortcut(person: Person, chatId: Long) {
    if (Build.VERSION.SDK_INT >= 25) {
      val shortcutBuilder = ShortcutInfoCompat.Builder(context, chatId.toString())

      if (Build.VERSION.SDK_INT >= 29)
        shortcutBuilder.setLongLived(true)

      val shortcut = shortcutBuilder.setIntent(
        mainScreenEventChannel.getEventIntent(MainScreenEvent.OpenChat(chatId = chatId)).setAction("OPEN_CHAT")
      )
        .setShortLabel(person.name ?: "Chat $chatId")
        .setIcon(person.icon)
        .setPerson(person)
        .build()

      if (Build.VERSION.SDK_INT >= 30)
        (context.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager)
          .pushDynamicShortcut(shortcut.toShortcutInfo())
    }
  }

  companion object {
    private const val TAG = "CHAT_MESSAGES"
    private const val CHANNEL_ID = "chat_messages"
    private const val GROUP_ID = "com.lofigroup.seeyau.chat_messages_group"
    private const val SUMMARY_ID: Int = 555555555
  }
}