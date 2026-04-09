package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@Composable
fun AppBackRow(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = Dimens.SpacingS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.atras),
                color = BrandGreen,
                style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline)
            )
        }
    }
}