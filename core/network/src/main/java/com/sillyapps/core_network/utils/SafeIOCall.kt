package com.sillyapps.core_network.utils

import com.sillyapps.core_network.getErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun safeIOCall(
  dispatcher: CoroutineDispatcher,
  block: suspend () -> Unit
) {
  withContext(dispatcher) {
    try {
      block()
    } catch (e: Exception) {
      Timber.e(getErrorMessage(e))
    }
  }
}

suspend fun <T> safeIOCall(
  dispatcher: CoroutineDispatcher,
  block: suspend () -> T,
  errorBlock: (Exception) -> T,
): T {
  return withContext(dispatcher) {
    try {
      block()
    } catch (e: Exception) {
      errorBlock(e)
    }
  }
}