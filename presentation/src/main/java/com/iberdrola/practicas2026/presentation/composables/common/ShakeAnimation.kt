package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing

@Composable
fun rememberShakeOffset(): Animatable<Float, AnimationVector1D> {
    return remember { Animatable(0f) }
}

suspend fun Animatable<Float, AnimationVector1D>.shake() {
    repeat(6) { index ->
        val target = if (index % 2 == 0) 3f else -3f
        animateTo(target, tween(50, easing = LinearEasing))
    }
    animateTo(0f, tween(50))
}