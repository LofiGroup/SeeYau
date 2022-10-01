package com.lofigroup.seeyau.domain.settings.usecases

import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.lofigroup.seeyau.domain.settings.model.Visibility
import javax.inject.Inject

class SetVisibilityUseCase @Inject constructor(
  private val repository: SettingsRepository
) {

  suspend operator fun invoke(visibility: Visibility) {
    return repository.setVisibility(visibility)
  }

}