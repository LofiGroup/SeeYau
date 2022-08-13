package com.lofigroup.data.navigator.remote.interceptors

import com.lofigroup.seeyau.domain.auth.usecases.GetTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AddAccessTokenInterceptor @Inject constructor(
  private val getTokenUseCase: GetTokenUseCase
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val token = getTokenUseCase()

    val url = originalRequest.url.newBuilder().addQueryParameter("access_token", token?.value)
      .build()

    val requestWithAccessToken = originalRequest.newBuilder().url(url).build()

    return chain.proceed(requestWithAccessToken)
  }
}