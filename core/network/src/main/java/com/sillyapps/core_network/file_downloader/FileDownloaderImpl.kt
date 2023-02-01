package com.sillyapps.core_network.file_downloader

import android.content.Context
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FileDownloaderImpl @Inject constructor(
  private val fileDownloaderApi: FileDownloaderApi,
  private val context: Context
): FileDownloader {
  override suspend fun downloadToInternalStorage(
    url: String,
    oldUri: String?
  ): String? = withContext(Dispatchers.IO) {
    try {
      if (oldUri != null) {
        Timber.e("Deleting file $oldUri")
        deleteFile(oldUri, context)
      }

      val body = fileDownloaderApi.downloadFile(url)
      Timber.e("Downloaded file $url")
      val file = createNewFile(context)
      writeToFile(file, body)
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
}