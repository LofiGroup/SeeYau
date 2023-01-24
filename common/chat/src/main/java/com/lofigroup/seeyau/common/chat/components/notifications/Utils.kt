package com.lofigroup.seeyau.common.chat.components.notifications

import android.content.Context
import android.graphics.*
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
import com.lofigroup.seeyau.common.chat.R
import com.lofigroup.seeyau.domain.profile.model.User
import timber.log.Timber

fun User.toPerson(context: Context): Person {
  val personBuilder = Person.Builder()
    .setName(context.getString(R.string.user, id))
    .setKey(id.toString())
  if (imageUrl != null) {
    val loader = Coil.imageLoader(context)

    val result = loader.executeBlocking(
      ImageRequest.Builder(context)
      .data(imageUrl)
      .transformations(CircleCropTransformation())
      .build())
    val bitmap = result.drawable?.toBitmap()
    if (bitmap != null)
      personBuilder.setIcon(IconCompat.createWithBitmap(bitmap))
  }

  return personBuilder.build()
}