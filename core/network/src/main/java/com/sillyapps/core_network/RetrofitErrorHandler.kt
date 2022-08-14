package com.sillyapps.core_network

import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import kotlin.Exception

fun <T> retrofitErrorHandler(res: Response<T>): T {
  if (res.isSuccessful) {
    if (res.body() == null) throw EmptyResponseBodyException()
    return res.body()!!
  } else {
    throw HttpException(res)
  }
}