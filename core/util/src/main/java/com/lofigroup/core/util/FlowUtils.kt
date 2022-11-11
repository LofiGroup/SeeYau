package com.lofigroup.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun timerFlow(interval: Long) = flow {
  emit(Unit)
  delay(interval)
}