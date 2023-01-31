package com.lofigroup.seeyau.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.seeyau.common.chat.components.notifications.ChatNotificationBuilder
import com.lofigroup.seeyau.common.profile.notifications.ProfileNotificationBuilder
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.data.AppDatabase
import com.lofigroup.seeyau.features.data_sync_service.DataSyncer
import com.lofigroup.seeyau.features.data_sync_service.di.DataSyncServiceComponent
import com.lofigroup.seeyau.main_screen_event_channel.MainScreenEventChannelModule
import com.lofigroup.seeyau.ui.MainViewModel
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.file_downloader.di.FileDownloaderModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@AppScope
@Component(modules = [PersistenceModule::class, IOModule::class, PermissionModule::class, NotificationRequesterModule::class, MainScreenEventChannelModule::class, BluetoothRequesterModule::class])
interface AppComponent {

  fun getSharedPref(): SharedPreferences

  fun getDatabase(): AppDatabase

  fun getContentResolver(): ContentResolver

  fun getPermissionChannel(): PermissionRequestChannel

  fun getNotificationRequester(): NotificationRequester

  fun getBluetoothRequester(): BluetoothRequesterChannel

  fun getMainScreenEventChannel(): MainScreenEventChannel

  fun getChatNotificationBuilder(): ChatNotificationBuilder

  fun getProfileNotificationBuilder(): ProfileNotificationBuilder

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun context(context: Context): Builder

    fun build(): AppComponent

  }

}