package com.lofigroup.data.navigator.local

import com.lofigroup.data.navigator.local.model.ProfileDataModel

interface ProfileDataSource {

  fun getProfile(): ProfileDataModel

}