package com.lofigroup.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun timerFlow(interval: Long) = flow {
  while (true) {
    emit(Unit)
    delay(interval)
  }
}