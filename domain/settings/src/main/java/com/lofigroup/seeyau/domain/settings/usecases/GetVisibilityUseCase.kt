package com.lofigroup.seeyau.domain.settings.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.lofigroup.seeyau.domain.settings.model.Visibility
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVisibilityUseCase @Inject constructor(
  private val repository: SettingsRepository
) {

  operator fun invoke(): Flow<Visibility> {
    return repository.getVisibility()
  }

}