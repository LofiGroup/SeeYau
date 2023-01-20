package com.lofigroup.seeyau.features.data_sync_service

import com.lofigroup.core.util.ResourceState
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.domain.navigator.usecases.ConnectToWebsocketUseCase
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.usecases.SendFirebaseTokenUseCase
import com.lofigroup.seeyau.domain.chat.usecases.PullChatDataUseCase
import com.lofigroup.seeyau.domain.chat.usecases.SendLocalMessagesUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullBlacklistDataUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullContactsUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullLikesUseCase
import com.lofigroup.seeyau.domain.profile.usecases.PullProfileDataUseCase
import com.lofigroup.seeyau.features.data_sync_service.notification.ChatMessagesNotificationBuilder
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class DataSyncer @Inject constructor(
  private val pullChatDataUseCase: PullChatDataUseCase,
  private val pullProfileDataUseCase: PullProfileDataUseCase,
  private val pullLikesUseCase: PullLikesUseCase,
  private val pullBlacklistDataUseCase: PullBlacklistDataUseCase,
  private val pullContactsUseCase: PullContactsUseCase,
  private val sendLocalMessagesUseCase: SendLocalMessagesUseCase,

  private val connectToWebsocketUseCase: ConnectToWebsocketUseCase,
  private val syncStateHolder: ResourceStateHolder,
  private val sendFirebaseTokenUseCase: SendFirebaseTokenUseCase,
  private val authModule: AuthModule,
  private val chatMessagesNotificationBuilder: ChatMessagesNotificationBuilder
) {

  private var syncing = false

  suspend fun syncWhenReady() {
    authModule.observeState().collect() {
      if (it == ResourceState.IS_READY) {
        connectToWebsocketUseCase()
        sync()
        sendFirebaseTokenUseCase()
      }
    }
  }

  suspend fun syncOnlyIfReady() {
    if (authModule.observeState().first() == ResourceState.IS_READY)
      sync(sendNotification = true)
  }

  private suspend fun sync(sendNotification: Boolean = false) {
    Timber.e("Syncing data...")
    if (syncing) return

    syncing = true
    pullBlacklistDataUseCase()
    pullProfileDataUseCase()
    pullContactsUseCase()
    pullLikesUseCase()
    val newMessages = pullChatDataUseCase(sendNotification)

    if (newMessages.isNotEmpty()) chatMessagesNotificationBuilder.sendNotification(newMessages)

    sendLocalMessagesUseCase()
    syncStateHolder.set(ResourceState.IS_READY)

    syncing = false
  }

}