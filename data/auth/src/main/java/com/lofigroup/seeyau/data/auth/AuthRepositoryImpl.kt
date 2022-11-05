package com.lofigroup.seeyau.data.auth

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.ResourceState
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.seeyau.data.auth.model.toAccessRequest
import com.lofigroup.seeyau.data.auth.model.toAuthResponse
import com.lofigroup.seeyau.data.auth.model.toStartAuthRequest
import com.lofigroup.seeyau.data.auth.model.toTokenDataModel
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.lofigroup.seeyau.domain.auth.model.LoggedInStatus
import com.lofigroup.seeyau.domain.auth.model.StartAuth
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val authApi: AuthApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val tokenStore: TokenStore,
  private val moduleStateHolder: ResourceStateHolder
) : AuthRepository {

  private var authOnlyToken: String? = null

  override suspend fun logout() {
    tokenStore.forgetToken()
  }

  override suspend fun authorize(access: Access): Resource<AuthResponse> {
    return safeIOCall(
      ioDispatcher,
      block = {
        val response =
          retrofitErrorHandler(authApi.authorize(access.toAccessRequest(), token = authOnlyToken!!))

        tokenStore.saveToken(response.toTokenDataModel())
        moduleStateHolder.set(ResourceState.IS_READY)

        Resource.Success(response.toAuthResponse())
      },
      errorBlock = {
        Timber.e(it)
        Resource.Error(it.message ?: "Unknown error")
      }
    )
  }

  override suspend fun startAuth(startAuth: StartAuth) = withContext(ioDispatcher) {
    return@withContext try {
      val response = retrofitErrorHandler(authApi.startAuth(startAuth.toStartAuthRequest()))

      authOnlyToken = response.token
      Resource.Success(Unit)
    } catch (e: EmptyResponseBodyException) {
      Resource.Success(Unit)
    } catch (e: Exception) {
      Resource.Error(getErrorMessage(e))
    }
  }

  override suspend fun check(): LoggedInStatus {
    return safeIOCall(
      ioDispatcher,
      block = {
        retrofitErrorHandler(authApi.check())

        moduleStateHolder.set(ResourceState.IS_READY)
        LoggedInStatus.LoggedIn
      },
      errorBlock = {
        val state = when (it) {
          is EmptyResponseBodyException -> {
            moduleStateHolder.set(ResourceState.IS_READY)
            LoggedInStatus.LoggedIn
          }
          is HttpException -> {
            when (it.code()) {
              408, 429, 502, 503 -> {
                moduleStateHolder.set(ResourceState.INITIALIZED)
                resolveLoggedInState()
              }
              401 -> LoggedInStatus.InvalidToken
              else -> LoggedInStatus.UnknownError(it.message())
            }
          }
          is SocketTimeoutException, is ConnectException -> {
            resolveLoggedInState()
          }
          else -> {
            LoggedInStatus.UnknownError(it.message ?: "Unknown error")
          }
        }
        Timber.e("Logged in status is $state")
        state
      }
    )
  }

  private fun resolveLoggedInState(): LoggedInStatus {
    return if (tokenStore.isEmpty()) LoggedInStatus.InvalidToken
    else LoggedInStatus.CantAccessServer
  }
}