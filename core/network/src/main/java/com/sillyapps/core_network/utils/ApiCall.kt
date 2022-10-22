package com.sillyapps.core_network.utils

import com.sillyapps.core_network.getErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun apiCall(
  dispatcher: CoroutineDispatcher,
  block: suspend () -> Unit,
) {
  withContext(dispatcher) {
    try {
      block()
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }
}