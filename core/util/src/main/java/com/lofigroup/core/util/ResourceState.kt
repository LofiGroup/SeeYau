package com.lofigroup.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

enum class ResourceState {
  LOADING, INITIALIZED, IS_READY
}

class ResourceStateHolder {
  private val state = MutableStateFlow(ResourceState.LOADING)

  fun observe(): Flow<ResourceState> {
    return state
  }

  fun set(resourceState: ResourceState) {
    state.value = resourceState
  }

}