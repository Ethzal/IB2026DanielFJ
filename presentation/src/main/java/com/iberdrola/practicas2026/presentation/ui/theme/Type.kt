package com.iberdrola.practicas2026.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.presentation.R

val AppFontFamily = FontFamily(
    Font(R.font.iberpangea_regular, FontWeight.Normal),
    Font(R.font.iberpangea_bold, FontWeight.Bold)
)

private val baseTextStyle = TextStyle(fontFamily = AppFontFamily)

val AppTypography = Typography(

    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextXXL,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
    ),

//    headlineSmall = TextStyle(
//        fontFamily = AppFontFamily,
//        fontSize = Dimens.TextXL,
//        fontWeight = FontWeight.Bold,
//    ),

    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextXL,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextL,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
    ),

//    bodyMedium = TextStyle(
//        fontFamily = AppFontFamily,
//        fontSize = Dimens.TextM,
//        fontWeight = FontWeight.Normal,
//    ),

    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextS,
        lineHeight = 16.sp,
    ),

    displaySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextAmount,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = Dimens.TextXS,
        fontWeight = FontWeight.Bold,
        lineHeight = 15.sp,
    ),

    bodyMedium = baseTextStyle.copy(fontSize = Dimens.TextM, lineHeight = 18.sp,),
    titleMedium = baseTextStyle.copy(fontWeight = FontWeight.Bold, lineHeight = 24.sp),
    labelMedium = baseTextStyle.copy(lineHeight = 16.sp,),
    displayLarge = baseTextStyle.copy(lineHeight = 44.sp,),
    displayMedium = baseTextStyle.copy(lineHeight = 40.sp,),
    headlineLarge = baseTextStyle.copy(lineHeight = 36.sp,),
    headlineSmall = baseTextStyle.copy(lineHeight = 28.sp,)
)