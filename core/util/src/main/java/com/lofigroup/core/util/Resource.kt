package com.lofigroup.core.util

sealed class Resource<T> {

  class Success<T>(val data: T): Resource<T>()

  class Loading<T>: Resource<T>()

  class Error<T>(val errorMessage: String): Resource<T>()

}

fun<T, M> Resource<T>.map(
  mappingFunction: (T) -> M
): Resource<M> {
  return when (this) {
    is Resource.Error -> Resource.Error(errorMessage)
    is Resource.Loading -> Resource.Loading()
    is Resource.Success -> Resource.Success(mappingFunction(data))
  }
}
