package com.iberdrola.practicas2026.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val AppTypography = Typography(

    headlineMedium = TextStyle(
        fontSize = Dimens.TextXXL,
        fontWeight = FontWeight.Bold,
        color = TextMain
    ),

    titleLarge = TextStyle(
        fontSize = Dimens.TextXL,
        fontWeight = FontWeight.Bold,
        color = TextMain
    ),

    bodyLarge = TextStyle(
        fontSize = Dimens.TextL,
        fontWeight = FontWeight.Bold,
        color = TextMain
    ),

    bodySmall = TextStyle(
        fontSize = Dimens.TextS,
        color = TextSecondary
    ),

    displaySmall = TextStyle(
        fontSize = Dimens.TextAmount,
        fontWeight = FontWeight.Bold,
        color = TextMain
    ),

    labelSmall = TextStyle(
        fontSize = Dimens.TextXS,
        fontWeight = FontWeight.Bold
    )
)