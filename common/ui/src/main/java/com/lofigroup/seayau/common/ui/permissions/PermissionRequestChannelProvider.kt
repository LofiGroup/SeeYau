package com.lofigroup.seayau.common.ui.permissions

interface PermissionRequestChannelProvider {
  fun providePermissionChannel(): PermissionRequestChannel
}