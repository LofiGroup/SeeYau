package com.lofigroup.seeyau.features.auth_screen_flow.ui

import android.net.Uri
import com.lofigroup.seeyau.features.auth_screen_flow.model.AuthScreenFlowModel
import com.lofigroup.seeyau.features.auth_screen_flow.model.RoutePoint
import kotlinx.coroutines.flow.Flow

interface AuthScreenFlowStateHolder {

  fun getState(): Flow<AuthScreenFlowModel>

  fun setName(name: String)

  fun setNumber(number: String)

  fun setCode(code: String)

  fun startAuth()

  fun setRoutePoint(routePoint: RoutePoint)

  fun setImageUri(uri: Uri)

  fun throwError(errorMessage: String)

}