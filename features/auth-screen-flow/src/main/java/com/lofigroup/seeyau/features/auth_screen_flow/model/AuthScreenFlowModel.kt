package com.lofigroup.seeyau.features.auth_screen_flow.model

import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.StartAuth

data class AuthScreenFlowModel(
  val name: String = "",
  val number: String = "",
  val code: String = "",
  val verifyCodeScreenState: VerifyCodeScreenState = VerifyCodeScreenState.TYPING,
  val enterNumberScreenState: EnterNumberScreenState = EnterNumberScreenState.TYPING,
  val routePoint: RoutePoint = RoutePoint.EnterName,
  val imageUri: String = "",
  val allDataIsValid: Boolean = false,
)

enum class RoutePoint {
  EnterName, EnterPhone, VerifyPhone, PickPicture
}

enum class VerifyCodeScreenState {
  TYPING, ERROR, SUCCESS, LOADING
}

enum class EnterNumberScreenState {
  TYPING, ERROR, LOADING
}

fun AuthScreenFlowModel.toAccess() =
  Access(
    code = code
  )

fun AuthScreenFlowModel.toStartAuth() =
  StartAuth(
    name = name,
    phoneNumber = number
  )