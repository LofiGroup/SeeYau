package com.lofigroup.seeyau.features.chat.ui.composition_locals

import androidx.compose.runtime.staticCompositionLocalOf
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DefaultFileDownloader: FileDownloader {
  override suspend fun downloadToInternalStorage(
    downloadId: String,
    url: String,
    oldUri: String?
  ): String? {
    return null
  }

  override fun downloadToExternalStorage(url: String) {}

  override fun subscribe(downloadId: String): Flow<DownloadProgress> = flow { DownloadProgress() }

  override fun unsubscribe(downloadId: String) {}

}

val LocalFileDownloader = staticCompositionLocalOf<FileDownloader> {
  DefaultFileDownloader
}