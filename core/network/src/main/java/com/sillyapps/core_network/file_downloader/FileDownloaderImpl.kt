package com.sillyapps.core_network.file_downloader

import android.content.Context
import androidx.core.content.FileProvider
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FileDownloaderImpl @Inject constructor(
  private val fileDownloaderApi: FileDownloaderApi,
  private val context: Context
): FileDownloader {
  private val ongoingDownloads = HashMap<String, MutableStateFlow<DownloadProgress>>()

  override suspend fun downloadToInternalStorage(
    downloadId: String,
    url: String,
    oldUri: String?
  ): String? = withContext(Dispatchers.IO) {
    try {
      if (oldUri != null) {
        Timber.e("Deleting file $oldUri")
        deleteFile(oldUri, context)
      }

      val download = getOrCreateDownload(downloadId)
      val body = fileDownloaderApi.downloadFile(url)

      val file = createNewFile(context)
      writeToFile(file, body, onDownloadProgress = {
        download.value = DownloadProgress(progress = it, isStarted = true)
      })
      Timber.e("Wrote to the file $url")

      return@withContext FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file).toString()
    } catch (e: Exception) {
      Timber.e(e)
      return@withContext null
    }
  }

  override fun downloadToExternalStorage(url: String) {
    //TODO implement
  }

  override fun subscribe(downloadId: String): Flow<DownloadProgress> {
    return getOrCreateDownload(downloadId)
  }

  override fun unsubscribe(downloadId: String) {
    val download = ongoingDownloads[downloadId] ?: return
    if (!download.value.isStarted) {
      ongoingDownloads.remove(downloadId)
    }
  }

  private fun getOrCreateDownload(downloadId: String): MutableStateFlow<DownloadProgress> {
    val download = ongoingDownloads[downloadId]

    return if (download != null) download else {
      val newDownload = MutableStateFlow(DownloadProgress())

      Timber.e("Creating new download channel: $downloadId")

      ongoingDownloads[downloadId] = newDownload
      newDownload
    }
  }


}