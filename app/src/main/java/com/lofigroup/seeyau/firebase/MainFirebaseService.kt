package com.lofigroup.seeyau.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lofigroup.seeyau.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MainFirebaseService: FirebaseMessagingService() {

  private val job = Job()
  private val scope = CoroutineScope(Dispatchers.IO + job)

  private val appModules by lazy {
    (application as App).appModules
  }

  override fun onNewToken(token: String) {
    val authRepository = appModules.authModuleImpl.domainComponent().getRepository()
    scope.launch {
      authRepository.sendFirebaseToken(token)
    }
  }

  override fun onMessageReceived(message: RemoteMessage) {
    Timber.e("New message received: ${message.data}")
    val action = message.data["action"] ?: return


    when (action) {
      "sync_data" -> {
        scope.launch {
          val authRepository = appModules.authModuleImpl.domainComponent().getRepository()
          authRepository.check()
          appModules.dataSyncer.syncOnlyIfReady()
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

}