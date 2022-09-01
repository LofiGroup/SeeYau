package com.sillyapps.core.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

// Use then you need to get viewmodel from composable function
@Composable
inline fun <reified T : ViewModel> daggerViewModel(
  key: String? = null,
  crossinline viewModelInstanceCreator: () -> T
): T = viewModel(
  modelClass = T::class.java,
  key = key,
  factory = object : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return viewModelInstanceCreator() as T
    }
  }
)

// Use then you need to get viewmodel from activity/fragment
class Factory<T: ViewModel>(private val create: () -> T): ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return create() as T
  }
}