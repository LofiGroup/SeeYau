package com.lofigroup.features.nearby_service

import java.nio.ByteBuffer

fun Long.toByteArray(): ByteArray {
  val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
  buffer.putLong(this)
  return buffer.array()
}

fun ByteArray.toLong(): Long {
  val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
  buffer.put(this, 0, this.size)
  buffer.flip()
  return buffer.long
}