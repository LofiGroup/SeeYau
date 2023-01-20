package com.lofigroup.seeyau.features.data_sync_service.data

import android.content.Context
import android.graphics.*
import android.graphics.ImageDecoder.OnHeaderDecodedListener
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.executeBlocking
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.data_sync_service.R
import timber.log.Timber


fun User.toPerson(context: Context, avatar: Bitmap?): Person {
  val personBuilder = Person.Builder()
    .setName(context.getString(R.string.user, id))
    .setKey(id.toString())
  if (imageUrl != null) {
    val loader = Coil.imageLoader(context)

    val result = loader.executeBlocking(ImageRequest.Builder(context)
      .data(imageUrl)
      .transformations(CircleCropTransformation())
      .build())
    val bitmap = result.drawable?.toBitmap()
    if (bitmap != null)
      personBuilder.setIcon(IconCompat.createWithBitmap(bitmap))
  }

  return personBuilder.build()
}

fun getBitmapFromContentUri(context: Context, uri: String?): Bitmap? {
  if (uri == null) return null

  val parsedUri = Uri.parse(uri)
  val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, parsedUri)) { decoder, info, source ->
      return@decodeBitmap decoder.setPostProcessor { it.reshapeToCircle() }
    }
  else MediaStore.Images.Media.getBitmap(context.contentResolver, parsedUri)

  Timber.e("Bitmap size is ${bitmap.width}, ${bitmap.height}")
  return bitmap
}

fun Canvas.reshapeToCircle(): Int {
  val path = Path()
  path.fillType = Path.FillType.INVERSE_EVEN_ODD
  path.addRoundRect(
    0f, 0f, width.toFloat(),
    height.toFloat(), (width / 2).toFloat(), (width / 2).toFloat(), Path.Direction.CW
  )
  val paint = Paint()
  paint.isAntiAlias = true
  paint.color = Color.TRANSPARENT
  paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
  drawPath(path, paint)
  return PixelFormat.TRANSLUCENT
}