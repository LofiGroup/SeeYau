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

    val requestWithBearer = originalRequest.newBuilder()
      .addHeader("Authorization", "Bearer ${token.value}")
      .build()

    return chain.proceed(requestWithBearer)
  }
}