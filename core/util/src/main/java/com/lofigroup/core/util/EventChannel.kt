package com.lofigroup.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class EventChannel<T> {

  private val channel = MutableSharedFlow<T>(extraBufferCapacity = 1)

  fun observe(): Flow<T> = channel

  fun notify(model: T) {
    channel.tryEmit(model)
  }

}