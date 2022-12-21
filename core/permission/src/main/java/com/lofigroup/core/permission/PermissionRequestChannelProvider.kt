package com.lofigroup.core.permission

interface PermissionRequestChannelProvider {
  fun providePermissionChannel(): PermissionRequestChannel
}