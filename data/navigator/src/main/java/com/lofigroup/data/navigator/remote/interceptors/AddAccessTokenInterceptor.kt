package com.lofigroup.data.navigator.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AddAccessTokenInterceptor @Inject constructor(

) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val url = originalRequest.url.newBuilder().addQueryParameter("access_token", "")
      .build()

    val requestWithAccessToken = originalRequest.newBuilder().url(url).build()

    return chain.proceed(requestWithAccessToken)
  }
}