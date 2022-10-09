package com.lofigroup.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

enum class ResourceState {
  LOADING, IS_READY
}

class ResourceStateHolder {
  private val state = MutableStateFlow(ResourceState.LOADING)

  fun observe(): Flow<ResourceState> {
    return state
  }

  fun setLoading() {
    state.value = ResourceState.LOADING
  }
  fun setIsReady() {
    state.value = ResourceState.IS_READY
  }
}