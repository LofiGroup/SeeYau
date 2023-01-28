package com.lofigroup.features.nearby_service.search_mode

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lofigroup.features.nearby_service.search_mode.model.SearchMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

class SearchModeDataSourceImpl @Inject constructor(): DefaultLifecycleObserver, SearchModeDataSource {

  private val state = MutableStateFlow<SearchMode>(SearchMode.Normal)

  override fun init() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  override fun destroy() {
    ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
  }

  override fun getState(): Flow<SearchMode> = state

  override fun onResume(owner: LifecycleOwner) {
    Timber.e("App goes into foreground, setting Normal mode")
    state.value = SearchMode.Normal
  }

  override fun onPause(owner: LifecycleOwner) {
    Timber.e("App goes into background, setting BatterySave mode")
    state.value = SearchMode.BatterySave
  }

}