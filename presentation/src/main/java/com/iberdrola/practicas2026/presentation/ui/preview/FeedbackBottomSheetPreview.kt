package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.presentation.composables.common.FeedbackBottomSheet
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(name = "Feedback - Valoración")
@Composable
fun FeedbackBottomSheetPreview() {
    EnergyAppTheme {
        FeedbackBottomSheet(
            showThanks = false,
            onRatingSelected = {},
            onLaterSelected = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Feedback - Agradecimiento")
@Composable
fun FeedbackBottomSheetThanksPreview() {
    EnergyAppTheme {
        FeedbackBottomSheet(
            showThanks = true,
            onRatingSelected = {},
            onLaterSelected = {},
            onDismiss = {}
        )
    }
}