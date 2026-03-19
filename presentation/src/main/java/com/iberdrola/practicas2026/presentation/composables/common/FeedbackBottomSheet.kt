package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(
    showThanks: Boolean,
    onRatingSelected: (Int) -> Unit,
    onLaterSelected: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingL)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showThanks) {
                // VISTA DE VALORACIÓN
                Text(
                    text = stringResource(R.string.tu_opinion_nos_importa),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingM))

                Text(
                    text = stringResource(R.string.recomiendes_esta_app),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Dimens.SpacingS)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = Dimens.SpacingL),
                    thickness = Dimens.StrokeDefault,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingM))

                // Fila de Emojis
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val ratings = listOf(
                        R.drawable.ic_very_bad,
                        R.drawable.ic_bad,
                        R.drawable.ic_neutral,
                        R.drawable.ic_good,
                        R.drawable.ic_happy
                    )

                    ratings.forEachIndexed { index, drawableRes ->
                        Image(
                            painter = painterResource(id = drawableRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .clickable { onRatingSelected(index) }
                                .padding(1.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingM))

                // Botón "Responder más tarde"
                Text(
                    text = stringResource(R.string.responder_mas_tarde),
                    modifier = Modifier
                        .clickable { onLaterSelected() }
                        .padding(Dimens.SpacingS),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = BrandGreen
                    )
                )
            } else {
                // VISTA DE AGRADECIMIENTO
                Icon(
                    painter = painterResource(id = R.drawable.ic_happy),
                    contentDescription = null,
                    tint = BrandGreen,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingM))
                
                Text(
                    text = stringResource(R.string.muchas_gracias),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrandGreen
                )
                
                Text(
                    text = stringResource(R.string.tu_valoracion_nos_ayuda_a_mejorar_dia_a_dia),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Dimens.SpacingS)
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingXL))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.cerrar), color = Color.White)
                }
            }
        }
    }
}