package com.sillyapps.core_network.file_downloader

import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import kotlinx.coroutines.flow.Flow

interface FileDownloader {

  suspend fun downloadToInternalStorage(downloadId: String, url: String, oldUri: String? = null): String?
  fun downloadToExternalStorage(url: String)

  fun subscribe(downloadId: String): Flow<DownloadProgress>
  fun unsubscribe(downloadId: String)

}