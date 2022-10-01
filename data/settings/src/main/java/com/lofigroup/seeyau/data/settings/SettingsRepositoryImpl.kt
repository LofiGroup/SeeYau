package com.lofigroup.seeyau.data.settings

import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.map
import com.lofigroup.seeyau.data.settings.datasources.VisibilitySettingDataSource
import com.lofigroup.seeyau.data.settings.model.toDataModel
import com.lofigroup.seeyau.data.settings.model.toDomainModel
import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.lofigroup.seeyau.domain.settings.model.Visibility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val visibilitySettingDataSource: VisibilitySettingDataSource
): SettingsRepository {

  override fun getVisibility(): Flow<Visibility> {
    return visibilitySettingDataSource.get().map { it.toDomainModel() }
  }

  override suspend fun setVisibility(visibility: Visibility) {
    visibilitySettingDataSource.set(visibility.toDataModel())
  }

}