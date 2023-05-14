package com.lofigroup.seeyau.features.chat.ui.composition_locals

import androidx.compose.runtime.staticCompositionLocalOf
import com.sillyapps.core_network.file_downloader.FileDownloader
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun provideChatMediaDownloader(
  fileDownloader: FileDownloader,
  startDownload: (Long) -> Unit
): ChatMediaDownloader {
  return object : ChatMediaDownloader {
    override fun subscribe(messageId: Long): Flow<DownloadProgress> {
      return fileDownloader.subscribe("message_${messageId}")
    }

    override fun unsubscribe(messageId: Long) {
      fileDownloader.unsubscribe("message_${messageId}")
    }

    override fun startDownload(messageId: Long) {
      startDownload(messageId)
    }

  }
}

interface ChatMediaDownloader {
  fun subscribe(messageId: Long): Flow<DownloadProgress>
  fun unsubscribe(messageId: Long)

  fun startDownload(messageId: Long)
}

object DefaultChatMediaDownloader: ChatMediaDownloader {
  override fun subscribe(messageId: Long): Flow<DownloadProgress> = flow { DownloadProgress() }

  override fun unsubscribe(messageId: Long) {}

  override fun startDownload(messageId: Long) {}

}

val LocalChatMediaDownloader = staticCompositionLocalOf<ChatMediaDownloader> {
  DefaultChatMediaDownloader
}