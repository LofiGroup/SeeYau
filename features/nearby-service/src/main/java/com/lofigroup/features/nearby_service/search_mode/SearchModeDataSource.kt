package com.lofigroup.features.nearby_service.search_mode

import com.lofigroup.features.nearby_service.search_mode.model.SearchMode
import kotlinx.coroutines.flow.Flow

interface SearchModeDataSource {

  fun init()

  fun destroy()

  fun getState(): Flow<SearchMode>

}