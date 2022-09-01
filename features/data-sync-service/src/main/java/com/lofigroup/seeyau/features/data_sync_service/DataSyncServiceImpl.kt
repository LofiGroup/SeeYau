package com.lofigroup.seeyau.features.data_sync_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.usecases.PullNavigatorDataUseCase
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.usecases.PullProfileDataUseCase
import com.lofigroup.seeyau.features.data_sync_service.di.DaggerDataSyncServiceComponent
import com.sillyapps.core.ui.service.ServiceBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DataSyncServiceImpl: Service(), DataSyncService {

  private val binder = LocalBinder()
  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private var syncing = false

  @Inject lateinit var pullChatDataUseCase: PullChatDataUseCase
  @Inject lateinit var pullNavigatorDataUseCase: PullNavigatorDataUseCase
  @Inject lateinit var pullProfileDataUseCase: PullProfileDataUseCase

  override fun onBind(intent: Intent?): IBinder {
    return binder
  }

  override fun onCreate() {
    super.onCreate()
    val component = DaggerDataSyncServiceComponent.builder()
      .chatComponent((application as ChatComponentProvider).provideChatComponent())
      .profileComponent((application as ProfileComponentProvider).provideProfileComponent())
      .navigatorComponent((application as NavigatorComponentProvider).provideNavigatorComponent())
      .build()

    component.inject(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    serviceJob.cancel()
  }

  inner class LocalBinder: Binder(), ServiceBinder<DataSyncService> {
    override fun getService(): DataSyncService = this@DataSyncServiceImpl
  }

  override fun sync() {
    Timber.d("Syncing data...")
    if (syncing) return
    scope.launch {
      syncing = true
      pullProfileDataUseCase()
      pullNavigatorDataUseCase()
      pullChatDataUseCase()
      syncing = false
    }
  }
}