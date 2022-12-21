package com.sillyapps.core_network.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import com.sillyapps.core_network.ContentUriRequestBody
import okhttp3.MultipartBody

fun createMultipartBody(name: String, rawUri: String?, contentResolver: ContentResolver): MultipartBody.Part? {
  val rawUri = rawUri ?: return null

  val uri = Uri.parse(rawUri)
  if (uri.scheme != "content" && uri.scheme != "file") return null

  if (!contentResolver.canRead(uri)) return null

  val fileExt = getFileExtFromUri(uri, contentResolver)

  return MultipartBody.Part.createFormData(
    name = name,
    filename = "$name.$fileExt",
    body = ContentUriRequestBody(contentResolver, uri)
  )
}

fun getFileExtFromUri(uri: Uri, contentResolver: ContentResolver): String {
  return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri)) ?: ""
}