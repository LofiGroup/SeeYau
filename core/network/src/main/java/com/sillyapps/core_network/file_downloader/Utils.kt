package com.sillyapps.core_network.file_downloader

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.lofigroup.core.util.generateRandomString
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

fun createNewFile(context: Context): File {
  val internalDir = File(context.filesDir, "internal_files")
  internalDir.mkdir()
  return File(internalDir, generateRandomString())
}

@Throws(IOException::class)
fun writeToFile(file: File, body: ResponseBody) {
  var inputStream: InputStream? = null
  var outputStream: OutputStream? = null

  try {
    inputStream = body.byteStream()
    outputStream = FileOutputStream(file)

    val fileReader = ByteArray(4096)

    while (true) {
      val read = inputStream.read(fileReader)
      if (read == -1) break

      outputStream.write(fileReader, 0, read)
    }
    outputStream.flush()
  } catch (e: IOException) {
    throw e
  } finally {
    inputStream?.close()
    outputStream?.close()
  }
}

fun deleteFile(uri: String, context: Context) {
  val file = File(uri)

  var selectionArgs = arrayOf(file.absolutePath)
  val where: String?
  val filesUri: Uri?

  if (android.os.Build.VERSION.SDK_INT >= 29) {
    filesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    where = MediaStore.Images.Media._ID + "=?"
    selectionArgs = arrayOf(file.name)
  } else {
    where = MediaStore.MediaColumns.DATA + "=?"
    filesUri = MediaStore.Files.getContentUri("external")
  }

  context.contentResolver.delete(filesUri!!, where, selectionArgs)
}