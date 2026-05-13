package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.DividerColor
import com.iberdrola.practicas2026.presentation.ui.theme.White

@Composable
fun WizardBottomBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    primaryEnabled: Boolean = true,
    primaryIcon: Int? = null,
    addGhostSpace: Boolean = false,
    secondaryEnabled: Boolean = true,
    interactionEnabled: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val paddingPx = Dimens.SpacingM.roundToPx()
                    val extendedWidth = constraints.maxWidth + (paddingPx * 2)

                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = extendedWidth,
                            maxWidth = extendedWidth
                        )
                    )
                    layout(constraints.maxWidth, placeable.height) {
                        placeable.place(-paddingPx, 0)
                    }
                },
            color = DividerColor,
            thickness = Dimens.DividerHeight
        )

        Spacer(Modifier.height(Dimens.SpacingM))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
        ) {
            // Botón Secundario (Anterior)
            if (secondaryText != null && onSecondaryClick != null) {
                OutlinedButton(
                    onClick = onSecondaryClick,
                    enabled = secondaryEnabled && interactionEnabled,
                    modifier = Modifier.weight(1f).height(50.dp),
                    border = BorderStroke(Dimens.StrokeThick, BrandGreen),
                    shape = RoundedCornerShape(Dimens.CornerButtonXL)
                ) {
                    Text(secondaryText, color = BrandGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Botón Principal (Siguiente / Modificar)
            Button(
                onClick = onPrimaryClick,
                enabled = interactionEnabled,
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (primaryEnabled) BrandGreen else BrandGreen.copy(alpha = 0.17f),
                    contentColor = if (primaryEnabled) White else BrandGreen.copy(alpha = 0.4f),
                    disabledContainerColor = BrandGreen.copy(alpha = 0.17f),
                    disabledContentColor = BrandGreen.copy(alpha = 0.4f),
                ),
                shape = RoundedCornerShape(Dimens.CornerButtonXL)
            ) {
                if (primaryIcon != null) {
                    Icon(painterResource(primaryIcon), null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(Dimens.SpacingS))
                }
                Text(primaryText, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (addGhostSpace) {
            Spacer(Modifier.height(Dimens.SpacingM + 50.dp))
        }
        Spacer(Modifier.height(Dimens.SpacingS))
    }
}