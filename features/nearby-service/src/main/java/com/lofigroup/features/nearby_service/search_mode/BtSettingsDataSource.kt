package com.lofigroup.features.nearby_service.search_mode

import com.lofigroup.features.nearby_service.search_mode.model.BtSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BtSettingsDataSource {

  fun getState(): StateFlow<BtSettings>

}