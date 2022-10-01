package com.lofigroup.seeyau.domain.settings

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.settings.model.Visibility
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

  fun getVisibility(): Flow<Visibility>

  suspend fun setVisibility(visibility: Visibility)

}