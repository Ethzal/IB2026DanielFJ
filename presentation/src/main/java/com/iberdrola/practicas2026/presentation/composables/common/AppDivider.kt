package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.DividerColor

@Composable
fun AppDivider() {
    HorizontalDivider(
        color = DividerColor,
        thickness = Dimens.DividerHeight
    )
}