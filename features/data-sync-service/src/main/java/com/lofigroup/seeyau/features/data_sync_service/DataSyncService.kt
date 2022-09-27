package com.lofigroup.seeyau.features.data_sync_service

import com.lofigroup.core.util.ResourceState
import kotlinx.coroutines.flow.Flow

interface DataSyncService {

  fun sync()

  fun getState(): Flow<DataSyncServiceState>

}