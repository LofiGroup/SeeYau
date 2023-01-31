package com.lofigroup.core.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun timerFlow(interval: Long) = flow {
  while (true) {
    emit(Unit)
    delay(interval)
  }
}

inline fun<T> MutableStateFlow<T>.set(
  transformation: (T) -> T
) {
  value = transformation(value)
}

fun CoroutineScope.cancelAndLaunch(
  dispatcher: CoroutineDispatcher = Dispatchers.Main,
  block: suspend () -> Unit
) {
  coroutineContext.cancelChildren()
  launch(dispatcher) { block() }
}

fun <T, K> StateFlow<T>.mapState(
  scope: CoroutineScope,
  initialValue: K,
  transform: suspend (data: T) -> K
): StateFlow<K> {
  return mapLatest { transform(it) }.stateIn(scope, SharingStarted.Eagerly, initialValue)
}