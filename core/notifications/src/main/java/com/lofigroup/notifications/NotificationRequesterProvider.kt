package com.lofigroup.notifications

interface NotificationRequesterProvider {
  fun provideNotificationRequester(): NotificationRequester
}