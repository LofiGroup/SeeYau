package com.lofigroup.seeyau.data.profile

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.data.profile.model.ProfileDataModel
import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {

  fun getMyId(): Long

  fun update(id: Long)

  fun isNotEmpty(): Boolean

}