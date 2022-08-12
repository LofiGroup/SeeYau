package com.sillyapps.core_network

import org.json.JSONObject
import retrofit2.Response
import kotlin.Exception

fun <T> retrofitErrorHandler(res: Response<T>): T {
  if (res.isSuccessful) {
    return res.body()!!
  } else {
    val errMsg = res.errorBody()?.string()?.let {
      JSONObject(it).getString("error")
    } ?: run {
      res.code().toString()
    }

    throw Exception(errMsg)
  }
}