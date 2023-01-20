package com.sillyapps.core_network.file_downloader

interface FileDownloader {

  suspend fun downloadToInternalStorage(url: String, oldUri: String? = null): String?
  fun downloadToExternalStorage(url: String)

}