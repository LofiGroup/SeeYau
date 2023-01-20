package com.sillyapps.core_network.file_downloader.di

import com.sillyapps.core.di.AppScope
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.file_downloader.FileDownloaderApi
import com.sillyapps.core_network.file_downloader.FileDownloaderImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object FileDownloaderModule {

  @AppScope
  @Provides
  fun provideFileDownloader(impl: FileDownloaderImpl): FileDownloader = impl

  @AppScope
  @Provides
  fun provideFileDownloaderApi(retrofit: Retrofit) = retrofit.create(FileDownloaderApi::class.java)

}