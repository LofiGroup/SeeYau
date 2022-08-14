package com.lofigroup.core.util

sealed class Result {
  object Success : Result()
  class Error(val message: String): Result()
}
