package com.lofigroup.seayau.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
  body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    lineHeight = 16.sp,
    fontSize = 14.sp
  ),
  h6 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 18.sp,
    fontSize = 14.sp
  ),
  h5 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 20.sp,
    fontSize = 20.sp,
  ),
  h4 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 24.sp,
    fontSize = 20.sp,
  ),
  caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W200,
    fontSize = 12.sp,
    textDecoration = TextDecoration.Underline
  ),
  subtitle1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 18.sp,
    lineHeight = 21.sp,
    color = DarkGray
  )
)

