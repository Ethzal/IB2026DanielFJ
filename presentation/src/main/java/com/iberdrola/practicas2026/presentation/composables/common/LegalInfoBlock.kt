package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain

@Composable
fun LegalInfoBlock(onInfoClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.info_basica_proteccion),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(Dimens.SpacingS))

        LegalInfoLine(baseText = stringResource(R.string.responsable_iberdrola), linkText = stringResource(R.string.mas_info), onInfoClick = onInfoClick)
        Spacer(Modifier.height(Dimens.SpacingS))
        LegalInfoLine(baseText = stringResource(R.string.finalidad_gestion), linkText = stringResource(R.string.mas_info), onInfoClick = onInfoClick)
        Spacer(Modifier.height(Dimens.SpacingS))
        LegalInfoLine(baseText = stringResource(R.string.derechos_acceso), linkText = stringResource(R.string.mas_info), onInfoClick = onInfoClick)
    }
}

@Composable
fun LegalInfoLine(baseText: String, linkText: String, onInfoClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append(baseText)
        append(" ")
        val start = length
        withStyle(SpanStyle(color = BrandGreen, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
            append(linkText)
        }
        val end = length
        addStringAnnotation(tag = "URL", annotation = "info", start = start, end = end)
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(color = TextMain),
        onTextLayout = { layoutResult = it },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { pos ->
                layoutResult?.let { layout ->
                    val offset = layout.getOffsetForPosition(pos)

                    annotatedString.getStringAnnotations("URL", offset, offset)
                        .firstOrNull()?.let {
                            onInfoClick()
                        }
                }
            }
        }
    )
}