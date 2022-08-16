package com.lofigroup.seeyau.data.auth

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.data.auth.model.toAccessRequest
import com.lofigroup.seeyau.data.auth.model.toToken
import com.lofigroup.seeyau.data.auth.model.toTokenDataModel
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.Token
import com.sillyapps.core_network.getErrorMessage
import com.sillyapps.core_network.retrofitErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val authApi: AuthApi,
  private val ioDispatcher: CoroutineDispatcher,
  private val tokenStore: TokenStore
): AuthRepository {
  override suspend fun login(access: Access) = withContext(ioDispatcher) {
    return@withContext try {
      val response = retrofitErrorHandler(authApi.login(access.toAccessRequest()))

      tokenStore.saveToken(response.toTokenDataModel())
      Resource.Success(Unit)
    }
    catch (e: Exception) {
      Resource.Error(getErrorMessage(e))
    }
  }

  override suspend fun logout() {
    tokenStore.forgetToken()
  }

  override suspend fun register(access: Access) = withContext(ioDispatcher) {
    return@withContext try {
      val response = retrofitErrorHandler(authApi.register(access.toAccessRequest()))

      tokenStore.saveToken(response.toTokenDataModel())
      Resource.Success(Unit)
    }
    catch (e: Exception) {
      Resource.Error(getErrorMessage(e))
    }
  }

  override suspend fun check() = withContext(ioDispatcher) {
    return@withContext try {
      retrofitErrorHandler(authApi.check())
      Result.Success
    } catch (e: HttpException) {
      Result.Error(e.message())
    } catch (e: Exception) {
      Result.Undefined(getErrorMessage(e))
    }
  }


}