package com.lofigroup.features.nearby_service

import com.lofigroup.core.util.ResourceState
import kotlinx.coroutines.flow.Flow

interface NearbyService {

  fun observeState(): Flow<ResourceState>

  fun start()

}