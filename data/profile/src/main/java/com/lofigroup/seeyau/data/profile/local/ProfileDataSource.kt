package com.lofigroup.seeyau.data.profile.local

interface ProfileDataSource {

  fun getMyId(): Long

  fun update(id: Long)

  fun isNotEmpty(): Boolean

}