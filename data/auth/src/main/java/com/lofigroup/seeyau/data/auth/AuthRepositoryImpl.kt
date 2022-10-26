package com.lofigroup.seeyau.data.auth

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.data.auth.model.toAccessRequest
import com.lofigroup.seeyau.data.auth.model.toAuthResponse
import com.lofigroup.seeyau.data.auth.model.toStartAuthRequest
import com.lofigroup.seeyau.data.auth.model.toTokenDataModel
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import com.lofigroup.seeyau.domain.auth.model.StartAuth
import com.sillyapps.core_network.exceptions.EmptyResponseBodyException
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import com.sillyapps.core_network.utils.safeIOCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val authApi: AuthApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val tokenStore: TokenStore,
  private val moduleStateHolder: ResourceStateHolder
): AuthRepository {

  private var authOnlyToken: String? = null

  override suspend fun logout() {
    tokenStore.forgetToken()
  }

  override suspend fun authorize(access: Access): Resource<AuthResponse> {
    return safeIOCall(
      ioDispatcher,
      block = {
        val response = retrofitErrorHandler(authApi.authorize(access.toAccessRequest(), token = authOnlyToken!!))

        tokenStore.saveToken(response.toTokenDataModel())
        moduleStateHolder.setIsReady()

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
    }
    catch (e: EmptyResponseBodyException) {
      Resource.Success(Unit)
    }
    catch (e: Exception) {
      Resource.Error(getErrorMessage(e))
    }
  }

  override suspend fun check() = withContext(ioDispatcher) {
    try {
      retrofitErrorHandler(authApi.check())
    } catch (e: EmptyResponseBodyException) {
      moduleStateHolder.setIsReady()
      return@withContext Result.Success
    } catch (e: HttpException) {
      return@withContext if (e.code() in listOf(401, 404)) {
        Result.Error(e.message())
      } else Result.Undefined(getErrorMessage(e))
    } catch (e: Exception) {
      return@withContext Result.Undefined(getErrorMessage(e))
    }

    return@withContext Result.Undefined("Unknown error")
  }
}