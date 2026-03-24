package com.iberdrola.practicas2026.presentation.composables.invoice

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.core.utils.toEpochMillis
import com.iberdrola.practicas2026.domain.model.InvoiceFilter
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreenLight
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    currentFilter: InvoiceFilter,
    amountBounds: ClosedFloatingPointRange<Float>,
    onApplyFilters: (InvoiceFilter) -> Unit,
    onClearFilters: () -> Unit,
    onBack: () -> Unit
) {
    var selectedFromDate by remember { mutableStateOf(currentFilter.dateFrom) }
    var selectedToDate by remember { mutableStateOf(currentFilter.dateTo) }

    // Si no hay rango seleccionado, coge los límites
    var sliderPosition by remember {
        mutableStateOf(currentFilter.amountRange ?: amountBounds)
    }

    val selectedStatuses = remember { mutableStateListOf(*currentFilter.statuses.toTypedArray()) }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    val statusOptions = remember { InvoiceStatus.all }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Dimens.SpacingM)
            .verticalScroll(rememberScrollState())
    ) {
        // Cabecera Atrás
        Column(modifier = Modifier
            .padding()
            .padding(top = 20.dp, start = 0.dp, end = 0.dp, bottom = Dimens.SpacingS)) {
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

        Text(stringResource(R.string.filtrar), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(Dimens.SpacingL))

        // FILTRO POR FECHA
        Text(stringResource(R.string.por_fecha), fontWeight = FontWeight.Bold, color = Color(0xFF333333))
        Spacer(Modifier.height(Dimens.SpacingM))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val density = LocalDensity.current

            // Campo "Desde"
            var fromFieldFocus by remember { mutableStateOf(false) }
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            if (isPressed) {
                showFromDatePicker = true
            }

            OutlinedTextField(
                value = selectedFromDate ?: "",
                onValueChange = {},
                readOnly = true,
                interactionSource = interactionSource,
                label = { Text(stringResource(R.string.desde), fontWeight = FontWeight.Normal) },
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showFromDatePicker = true }
                    .onFocusChanged { fromFieldFocus = it.isFocused }
                    .drawBehind {
                        val borderColor = if (fromFieldFocus) BrandGreen else Color.Gray
                        val strokeDp = if (fromFieldFocus) 2.dp else 1.dp
                        val strokePx = with(density) { strokeDp.toPx() }
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokePx
                        )
                    },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            // Campo "Hasta"
            var toFieldFocus by remember { mutableStateOf(false) }
            val interactionSourceTo = remember { MutableInteractionSource() }
            val isPressedTo by interactionSourceTo.collectIsPressedAsState()

            if (isPressedTo) {
                showToDatePicker = true
            }

            OutlinedTextField(
                value = selectedToDate ?: "",
                onValueChange = {},
                readOnly = true,
                interactionSource = interactionSourceTo,
                label = { Text(stringResource(R.string.hasta), fontWeight = FontWeight.Normal) },
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showToDatePicker = true }
                    .onFocusChanged { toFieldFocus = it.isFocused }
                    .drawBehind {
                        val borderColor = if (toFieldFocus) BrandGreen else Color.Gray
                        val strokeDp = if (toFieldFocus) 2.dp else 1.dp
                        val strokePx = with(density) { strokeDp.toPx() }
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokePx
                        )
                    },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(Modifier.height(Dimens.SpacingXL))

        // FILTRO POR IMPORTE
        Text(stringResource(R.string.por_un_importe), fontWeight = FontWeight.Bold, color = Color(0xFF333333))
        Spacer(Modifier.height(Dimens.SpacingM))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Surface(color = BrandGreenLight, shape = RoundedCornerShape(4.dp)) {
                Text(
                    text = "${sliderPosition.start.roundToInt()} € - ${sliderPosition.endInclusive.roundToInt()} €",
                    color = TextMain,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            // FILTRO POR IMPORTE
            RangeSlider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = amountBounds,
                // Definimos los colores base
                colors = SliderDefaults.colors(
                    thumbColor = BrandGreen,
                    activeTrackColor = BrandGreen,
                    inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                ),
                // Track personalizado
                track = { rangeSliderState ->
                    SliderDefaults.Track(
                        rangeSliderState = rangeSliderState,
                        modifier = Modifier.height(4.dp),
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = null,
                        colors = SliderDefaults.colors(
                            activeTrackColor = BrandGreen,
                            inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                },
                startThumb = {
                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = RoundedCornerShape(50),
                        color = BrandGreen,
                    ) {}
                },
                endThumb = {
                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = RoundedCornerShape(50),
                        color = BrandGreen,
                    ) {}
                }
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.euro_symbol, amountBounds.start.roundToInt()), color = Color.Gray, fontWeight = FontWeight.Normal)
                Text(stringResource(R.string.euro_symbol, amountBounds.endInclusive.roundToInt()), color = Color.Gray, fontWeight = FontWeight.Normal)
            }
        }

        Spacer(Modifier.height(Dimens.SpacingXL))

        // FILTRO POR ESTADO
        Text(stringResource(R.string.por_estado), fontWeight = FontWeight.Bold, color = Color(0xFF333333))
        Spacer(Modifier.height(Dimens.SpacingS))

        statusOptions.forEach { status ->
            val label = status.getLabel()
            val id = status.id

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedStatuses.contains(id)) selectedStatuses.remove(id)
                        else selectedStatuses.add(id)
                    }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedStatuses.contains(id),
                    onCheckedChange = { isChecked ->
                        if (isChecked) selectedStatuses.add(id)
                        else selectedStatuses.remove(id)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = BrandGreen,
                        uncheckedColor = BrandGreen
                    )
                )
                Text(text = label, color = Color(0xFF333333), fontWeight = FontWeight.Normal)
            }
        }

        Spacer(Modifier.weight(1f))

        // BOTONES
        Button(
            onClick = {
                onApplyFilters(InvoiceFilter(
                    dateFrom = selectedFromDate,
                    dateTo = selectedToDate,
                    amountRange = sliderPosition,
                    statuses = selectedStatuses.toSet()
                ))
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
        ) {
            Text(stringResource(R.string.aplicar_filtros), color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(Dimens.SpacingM))

        TextButton(
            onClick = {
                onClearFilters()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.borrar_filtros), color = BrandGreen, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
        }

        Spacer(Modifier.height(Dimens.SpacingM))
    }

    // Date Picker "Desde"
    if (showFromDatePicker) {
        DatePickerModal(
            initialDate = selectedFromDate,
            maxDateMillis = System.currentTimeMillis(),
            onDateSelected = {
                selectedFromDate = it
                val fromMillis = it.toEpochMillis() ?: 0
                val toMillis = selectedToDate.toEpochMillis() ?: 0
                if (toMillis < fromMillis) selectedToDate = null

                showFromDatePicker = false
            },
            onDismiss = { showFromDatePicker = false }
        )
    }

    // Date Picker "Hasta"
    if (showToDatePicker) {
        DatePickerModal(
            initialDate = selectedToDate,
            minDateMillis = selectedFromDate.toEpochMillis(),
            maxDateMillis = System.currentTimeMillis(),
            onDateSelected = {
                selectedToDate = it
                showToDatePicker = false
            },
            onDismiss = { showToDatePicker = false }
        )
    }
    Log.d("showToDatePicker", showToDatePicker.toString())
    Log.d("showFromDatePicker", showFromDatePicker.toString())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: String?,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = System.currentTimeMillis(),
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toEpochMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val isBeforeMax = maxDateMillis?.let { utcTimeMillis <= it } ?: true
                val isAfterMin = minDateMillis?.let { utcTimeMillis >= it } ?: true
                return isBeforeMax && isAfterMin
            }

            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = java.time.LocalDate.now().year
                return year <= currentYear
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                }
            }) { Text(stringResource(R.string.ok), color = BrandGreen) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancelar), color = BrandGreen) } }
    ) {
        DatePicker(state = datePickerState)
    }
}