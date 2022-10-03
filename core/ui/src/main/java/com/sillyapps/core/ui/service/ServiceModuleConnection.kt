package com.sillyapps.core.ui.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import timber.log.Timber

interface ServiceBinder<out T> {
  fun getService(): T
}

class ServiceModuleConnection<T>(
  private val serviceImplClass: Class<*>,
  private val serviceIsConnected: (T) -> Unit
) {
  private var isBound = false
  var boundService: T? = null

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
      Timber.d("Service ${this::class.java.simpleName} is connected")
      val binder = service as ServiceBinder<T>
      boundService = binder.getService()
      serviceIsConnected(boundService!!)
      isBound = true
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
      boundService = null
      isBound = false
    }
  }

  fun bind(context: Context) {
    if (isBound) return
    Intent(context, serviceImplClass).also {
      context.bindService(it, connection, Context.BIND_AUTO_CREATE)
    }
  }

  fun unbind(context: Context) {
    if (!isBound) return
    context.unbindService(connection)
    boundService = null
  }

}