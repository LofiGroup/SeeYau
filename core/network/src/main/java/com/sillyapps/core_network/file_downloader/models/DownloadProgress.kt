package com.sillyapps.core_network.file_downloader.models

data class DownloadProgress(
  val progress: Float = 0f,
  val isStarted: Boolean = false
)
