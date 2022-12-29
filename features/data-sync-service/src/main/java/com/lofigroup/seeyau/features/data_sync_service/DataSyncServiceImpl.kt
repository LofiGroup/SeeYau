package com.lofigroup.seeyau.features.data_sync_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lofigroup.core.util.ResourceState
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.usecases.ConnectToWebsocketUseCase
import com.lofigroup.seeyau.domain.auth.api.AuthModuleProvider
import com.lofigroup.seeyau.domain.base.api.BaseComponentProvider
import com.lofigroup.seeyau.domain.base.di.BaseModuleComponent
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.domain.chat.usecases.SendLocalMessagesUseCase
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.usecases.PullBlacklistDataUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullContactsUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullLikesUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullProfileDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import com.lofigroup.seeyau.features.data_sync_service.di.DaggerDataSyncServiceComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DataSyncServiceImpl: Service(), DataSyncService {

  private val binder = LocalBinder()
  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private var syncing = false

  @Inject lateinit var pullChatDataUseCase: PullChatDataUseCase
  @Inject lateinit var pullProfileDataUseCase: PullProfileDataUseCase
  @Inject lateinit var pullLikesUseCase: PullLikesUseCase
  @Inject lateinit var pullBlacklistDataUseCase: PullBlacklistDataUseCase
  @Inject lateinit var pullContactsUseCase: PullContactsUseCase
  @Inject lateinit var sendLocalMessagesUseCase: SendLocalMessagesUseCase

  @Inject lateinit var connectToWebsocketUseCase: ConnectToWebsocketUseCase

  @Inject lateinit var syncStateHolder: ResourceStateHolder

  override fun onBind(intent: Intent?): IBinder {
    return binder
  }

  override fun onCreate() {
    super.onCreate()

    Timber.e("Creating datasync service")

    val authModule = ((application as AuthModuleProvider).provideAuthModule())
    scope.launch {
      authModule.observeState().collect() {
        when (it) {
          ResourceState.IS_READY -> {
            init()
          }
          ResourceState.INITIALIZED -> {
          }
          else -> {}
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.e("Destroying NearbyService")
    serviceJob.cancel()
  }

  inner class LocalBinder: Binder() {
    fun getService(): DataSyncService = this@DataSyncServiceImpl
  }

  override fun sync() {
    Timber.d("Syncing data...")
    if (syncing) return
    scope.launch {
      syncing = true
      pullBlacklistDataUseCase()
      pullProfileDataUseCase()
      pullContactsUseCase()
      pullLikesUseCase()
      pullChatDataUseCase()

      connectToWebsocketUseCase()
      sendLocalMessagesUseCase()

      syncStateHolder.set(ResourceState.IS_READY)
      syncing = false
    }
  }

  private fun init() {
    val component = DaggerDataSyncServiceComponent.builder()
      .chatComponent((application as ChatComponentProvider).provideChatComponent())
      .profileComponent((application as ProfileComponentProvider).provideProfileComponent())
      .navigatorComponent((application as NavigatorComponentProvider).provideNavigatorComponent())
      .baseComponent((application as BaseComponentProvider).provideBaseComponent())
      .build()

    component.inject(this)
    Timber.e("DataSyncService is initialized")
    sync()
  }
}