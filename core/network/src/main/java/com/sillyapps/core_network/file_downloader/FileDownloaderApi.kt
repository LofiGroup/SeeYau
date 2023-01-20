package com.sillyapps.core_network.file_downloader

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileDownloaderApi {

  @Streaming
  @GET
  suspend fun downloadFile(@Url url: String): ResponseBody

}