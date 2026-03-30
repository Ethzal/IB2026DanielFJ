package com.iberdrola.practicas2026.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val AppTypography = Typography(

    headlineMedium = TextStyle(
        fontSize = Dimens.TextXXL,
        fontWeight = FontWeight.Bold,
    ),

    titleLarge = TextStyle(
        fontSize = Dimens.TextXL,
        fontWeight = FontWeight.Bold,
    ),

    bodyLarge = TextStyle(
        fontSize = Dimens.TextL,
        fontWeight = FontWeight.Bold,
    ),

    bodySmall = TextStyle(
        fontSize = Dimens.TextS,
    ),

    displaySmall = TextStyle(
        fontSize = Dimens.TextAmount,
        fontWeight = FontWeight.Bold,
    ),

    labelSmall = TextStyle(
        fontSize = Dimens.TextXS,
        fontWeight = FontWeight.Bold
    )
)