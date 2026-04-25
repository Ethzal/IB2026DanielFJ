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
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.White

@Composable
fun WizardBottomBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    primaryEnabled: Boolean = true,
    primaryIcon: Int? = null,
    addGhostSpace: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        Spacer(Modifier.height(Dimens.SpacingM))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingM),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
        ) {
            // Botón Secundario (Anterior)
            if (secondaryText != null && onSecondaryClick != null) {
                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    border = BorderStroke(1.dp, BrandGreen),
                    shape = RoundedCornerShape(Dimens.CornerButton)
                ) {
                    Text(secondaryText, color = BrandGreen, fontWeight = FontWeight.Bold)
                }
            }

            // Botón Principal (Siguiente / Modificar)
            Button(
                onClick = onPrimaryClick,
                enabled = primaryEnabled,
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGreen,
                    contentColor = White,
                    disabledContainerColor = BrandGreen.copy(alpha = 0.15f),
                    disabledContentColor = BrandGreen.copy(alpha = 0.4f),
                ),
                shape = RoundedCornerShape(Dimens.CornerButton)
            ) {
                if (primaryIcon != null) {
                    Icon(painterResource(primaryIcon), null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text(primaryText, fontWeight = FontWeight.Bold)
            }
        }

        if (addGhostSpace) {
            Spacer(Modifier.height(Dimens.SpacingM + 50.dp))
        }
        Spacer(Modifier.height(Dimens.SpacingS))
    }
}