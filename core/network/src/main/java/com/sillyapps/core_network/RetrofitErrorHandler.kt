package com.sillyapps.core_network

import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

fun <T> retrofitErrorHandler(res: Response<T>): T {
  if (res.isSuccessful) {
    if (res.body() == null) throw EmptyResponseBodyException()
    return res.body()!!
  } else {
    throw HttpException(res)
  }
}

fun getErrorMessage(e: Exception): String {
  return when (e) {
    is IOException -> "Couldn't connect to server"
    is HttpException -> e.message ?: "Http exception. No error message is provided"
    else -> "Unknown error. Error message: ${e.message}"
  }
}