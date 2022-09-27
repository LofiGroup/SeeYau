package com.lofigroup.seeyau.features.data_sync_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lofigroup.core.util.ResourceState
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.usecases.PullNavigatorDataUseCase
import com.lofigroup.seeyau.domain.auth.api.AuthModuleProvider
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.usecases.PullProfileDataUseCase
import com.lofigroup.seeyau.features.data_sync_service.di.DaggerDataSyncServiceComponent
import com.sillyapps.core.ui.service.ServiceBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
  @Inject lateinit var pullNavigatorDataUseCase: PullNavigatorDataUseCase
  @Inject lateinit var pullProfileDataUseCase: PullProfileDataUseCase

  private val state = MutableStateFlow(DataSyncServiceState.LOADING)

  override fun onBind(intent: Intent?): IBinder {
    return binder
  }

  override fun onCreate() {
    super.onCreate()

    val authModule = ((application as AuthModuleProvider).provideAuthModule())
    scope.launch {
      authModule.observeState().collect() {
        when (it) {
          ResourceState.LOADING -> {}
          ResourceState.IS_READY -> {
            init()
          }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    serviceJob.cancel()
  }

  inner class LocalBinder: Binder(), ServiceBinder<DataSyncService> {
    override fun getService(): DataSyncService = this@DataSyncServiceImpl
  }

  override fun sync() {
    if (state.value == DataSyncServiceState.LOADING) {
      Timber.e("DataSync service is not initialized!")
      return
    }
    Timber.d("Syncing data...")
    if (syncing) return
    scope.launch {
      syncing = true
      pullProfileDataUseCase()
      pullNavigatorDataUseCase()
      pullChatDataUseCase()
      state.value = DataSyncServiceState.SYNCED
    }
  }

  override fun getState(): Flow<DataSyncServiceState> {
    return state
  }

  private fun init() {
    if (state.value == DataSyncServiceState.INITIALIZED) return

    val component = DaggerDataSyncServiceComponent.builder()
      .chatComponent((application as ChatComponentProvider).provideChatComponent())
      .profileComponent((application as ProfileComponentProvider).provideProfileComponent())
      .navigatorComponent((application as NavigatorComponentProvider).provideNavigatorComponent())
      .build()

    component.inject(this)
    Timber.e("DataSyncService is initialized")
    state.value = DataSyncServiceState.INITIALIZED
    sync()
  }
}