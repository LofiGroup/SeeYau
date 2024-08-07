package com.lofigroup.seeyau.common.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lofigroup.seeyau.common.ui.R

// Set of Material typography styles to start with
val Typography = Typography(
  body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    lineHeight = 18.sp,
    fontSize = 14.sp
  ),
  body2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    lineHeight = 22.sp,
    fontSize = 17.sp
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
    lineHeight = 22.sp,
    fontSize = 14.sp
  ),
  h4 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 25.sp,
    fontSize = 20.sp,
  ),
  h3 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 20.sp,
    fontSize = 20.sp,
  ),
  h2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    lineHeight = 24.sp,
    fontSize = 20.sp,
  ),
  h1 = TextStyle(
    fontFamily = FontFamily(Font(R.font.swis721blkbtrusbyme_black)),
    fontWeight = FontWeight.W400,
    lineHeight = 28.sp,
    fontSize = 28.sp,
  ),
  caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 12.sp,
    lineHeight = 22.sp
  ),
  subtitle1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 18.sp,
    lineHeight = 21.sp
  ),
  subtitle2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 15.sp,
    lineHeight = 22.sp
  )
)

