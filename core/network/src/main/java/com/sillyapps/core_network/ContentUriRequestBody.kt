package com.sillyapps.core_network

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.IOException
import okio.source
import java.io.FileNotFoundException

class ContentUriRequestBody(
  private val contentResolver: ContentResolver,
  private val contentUri: Uri
): RequestBody() {

  override fun contentLength(): Long {
    return contentUri.length(contentResolver)
  }

  override fun contentType(): MediaType? {
    val contentType = contentResolver.getType(contentUri)
    return contentType?.toMediaTypeOrNull()
  }

  override fun writeTo(sink: BufferedSink) {
    val inputStream = contentResolver.openInputStream(contentUri)
      ?: throw IOException("Couldn't open content URI for reading")
    inputStream.source().use { source ->
      sink.writeAll(source)
    }
    inputStream.close()
  }

}

fun Uri.length(contentResolver: ContentResolver): Long {
  val assetFileDescriptor = try {
    contentResolver.openAssetFileDescriptor(this, "r")
  }
  catch (e: FileNotFoundException) {
    null
  }
  // uses ParcelFileDescriptor#getStatSize underneath if failed
  val length = assetFileDescriptor?.use { it.length } ?: -1L
  if (length != -1L) {
    return length
  }

  // if "content://" uri scheme, try contentResolver table
  if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
    return contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
      ?.use { cursor ->
        // maybe shouldn't trust ContentResolver for size: https://stackoverflow.com/questions/48302972/content-resolver-returns-wrong-size
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (sizeIndex == -1) {
          return@use -1L
        }
        cursor.moveToFirst()
        return try {
          cursor.getLong(sizeIndex)
        } catch (_: Throwable) {
          -1L
        }
      } ?: -1L
  } else {
    return -1L
  }
}