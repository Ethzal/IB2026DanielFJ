package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@Composable
fun ShimmerItem() {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.2f), Color.LightGray.copy(alpha = 0.6f)),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Column(modifier = Modifier.fillMaxWidth().padding(Dimens.SpacingM)) {
        Box(modifier = Modifier.fillMaxWidth().height(Dimens.ShimmerCardHeight).background(brush, RoundedCornerShape(Dimens.CornerDefault)))
        Spacer(modifier = Modifier.height(Dimens.SpacingL))
        Box(modifier = Modifier.width(Dimens.ShimmerTitleWidth).height(20.dp).background(brush))
        Spacer(modifier = Modifier.height(Dimens.SpacingM))
        Box(modifier = Modifier.fillMaxWidth().height(Dimens.ShimmerItemHeight).background(brush))
    }
}