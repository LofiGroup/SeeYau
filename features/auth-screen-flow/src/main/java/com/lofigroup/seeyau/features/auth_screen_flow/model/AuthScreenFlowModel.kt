package com.lofigroup.seeyau.features.auth_screen_flow.model

import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.StartAuth

data class AuthScreenFlowModel(
  val name: String = "",
  val number: String = "",
  val code: String = "",
  val verifyCodeScreenState: VerifyCodeScreenState = VerifyCodeScreenState.TYPING,
  val enterNumberScreenState: EnterNumberScreenState = EnterNumberScreenState.TYPING,
  val routePoint: RoutePoint = RoutePoint.Welcome,
  val imageUri: String = "",
  val flowState: AuthFlowState = AuthFlowState.WAITING_FOR_INPUT,

  )

enum class RoutePoint {
  Welcome, EnterName, EnterPhone, VerifyPhone, PickPicture, AlreadyRegistered
}

enum class VerifyCodeScreenState {
  TYPING, ERROR, SUCCESS, LOADING
}

enum class EnterNumberScreenState {
  TYPING, ERROR, LOADING
}

enum class AuthFlowState {
  WAITING_FOR_INPUT, SYNCING_DATA, ALL_DATA_IS_VALID, ERROR
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