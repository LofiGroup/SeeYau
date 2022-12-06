package com.lofigroup.seeyau.common.ui.permissions

interface PermissionRequestChannelProvider {
  fun providePermissionChannel(): PermissionRequestChannel
}