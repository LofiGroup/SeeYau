package com.lofigroup.seeyau.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.seeyau.features.data_sync_service.DataSyncer
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val dataSyncer: DataSyncer
): ViewModel() {

  init {
    viewModelScope.launch {
      dataSyncer.syncWhenReady()
    }
  }

}