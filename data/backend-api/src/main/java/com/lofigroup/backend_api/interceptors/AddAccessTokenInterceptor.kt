package com.lofigroup.backend_api.interceptors

import com.lofigroup.backend_api.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AddAccessTokenInterceptor @Inject constructor(
  private val tokenStore: TokenStore
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val token = tokenStore.getToken() ?: return chain.proceed(originalRequest)

    val url = originalRequest.url.newBuilder().addQueryParameter("access_token", token.value)
      .build()

    val requestWithAccessToken = originalRequest.newBuilder().url(url).build()

    return chain.proceed(requestWithAccessToken)
  }
}