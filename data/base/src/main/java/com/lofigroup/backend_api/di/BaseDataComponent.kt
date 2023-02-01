package com.lofigroup.backend_api.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.TokenStore
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.core.util.ResourceStateHolder
import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.file_downloader.di.FileDownloaderModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier

@AppScope
@Component(modules = [ApiModule::class, TokenStoreModule::class, FileDownloaderModule::class])
interface BaseDataComponent {

  fun getRetrofit(): Retrofit
  fun getHttpClient(): OkHttpClient

  fun getWebSocketChannel(): WebSocketChannel
  fun tokenStore(): TokenStore
  fun getFileDownloader(): FileDownloader
  @DataSyncStateHolder fun syncStateHolder(): ResourceStateHolder

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun moduleStateHolder(@DataSyncStateHolder moduleStateHolder: ResourceStateHolder): Builder

    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun appCoroutineScope(scope: CoroutineScope): Builder

    fun build(): BaseDataComponent
  }

}

@Qualifier
annotation class DataSyncStateHolder