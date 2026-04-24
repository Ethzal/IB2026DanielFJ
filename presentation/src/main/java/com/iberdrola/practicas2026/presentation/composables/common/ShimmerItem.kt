package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    return Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
}

@Composable
fun ShimmerLastInvoiceCard(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.SpacingXL),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(Dimens.CornerDefault)
    ) {
        Column(modifier = Modifier.padding(Dimens.SpacingM)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    // Última factura
                    Box(modifier = Modifier.width(100.dp).height(16.dp).background(brush))
                    Spacer(Modifier.height(Dimens.SpacingS))
                    // Tipo de factura
                    Box(modifier = Modifier.width(80.dp).height(12.dp).background(brush))
                }
                // Icono
                Box(modifier = Modifier.size(Dimens.IconM).background(brush, RoundedCornerShape(4.dp)))
            }
            Spacer(Modifier.height(Dimens.SpacingM))
            // Amount (€)
            Box(modifier = Modifier.width(120.dp).height(24.dp).background(brush))
            Spacer(Modifier.height(Dimens.SpacingXS))
            // Dates
            Box(modifier = Modifier.width(150.dp).height(12.dp).background(brush))

            HorizontalDivider(modifier = Modifier.padding(top = Dimens.SpacingM, bottom = Dimens.SpacingS), color = Color(0xFFF2F2F2))

            Spacer(Modifier.height(Dimens.SpacingS))
            // Status Pill
            Box(modifier = Modifier.width(90.dp).height(24.dp).background(brush, RoundedCornerShape(8.dp)))
        }
    }
}

@Composable
fun ShimmerInvoiceRow(brush: Brush) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = Dimens.SpacingM)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                // Fecha
                Box(modifier = Modifier.width(100.dp).height(18.dp).background(brush))
                Spacer(Modifier.height(4.dp))
                // Tipo
                Box(modifier = Modifier.width(70.dp).height(12.dp).background(brush))
                Spacer(Modifier.height(Dimens.SpacingS))
                // Status Pill
                Box(modifier = Modifier.width(90.dp).height(24.dp).background(brush, RoundedCornerShape(8.dp)))
            }
            // Amount + Arrow
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(60.dp).height(18.dp).background(brush))
                Spacer(Modifier.width(Dimens.SpacingM))
                Box(modifier = Modifier.size(20.dp).background(brush, CircleShape))
            }
        }
        Spacer(Modifier.height(Dimens.SpacingM))
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFF2F2F2))
    }
}

// El Botón de Filtrar Shimmer (Outline + Icono + Texto)
@Composable
fun ShimmerFilterButton(brush: Brush) {
    Surface(
        modifier = Modifier
            .height(40.dp)
            .width(95.dp),
        shape = RoundedCornerShape(Dimens.CornerButton),
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen.copy(alpha = 0.1f)),
        color = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            // Icono del filtro
            Box(modifier = Modifier.size(18.dp).background(brush, RoundedCornerShape(4.dp)))
            Spacer(Modifier.width(Dimens.SpacingS))
            // Texto "Filtrar"
            Box(modifier = Modifier.width(50.dp).height(14.dp).background(brush))
        }
    }
}

// Año
@Composable
fun ShimmerYearHeader(brush: Brush) {
    Box(
        modifier = Modifier
            .padding(vertical = Dimens.SpacingM)
            .width(40.dp)
            .height(16.dp)
            .background(brush)
    )
}