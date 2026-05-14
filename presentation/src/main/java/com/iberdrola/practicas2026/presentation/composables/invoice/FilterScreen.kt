package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.core.utils.formatSpanishInteger
import com.iberdrola.practicas2026.core.utils.toEpochMillis
import com.iberdrola.practicas2026.domain.model.InvoiceFilter
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.mapper.toUiModel
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreenLight
import com.iberdrola.practicas2026.presentation.ui.theme.DarkGray
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import com.iberdrola.practicas2026.presentation.ui.theme.White
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    currentFilter: InvoiceFilter,
    amountBounds: ClosedFloatingPointRange<Float>,
    minDateAllowed: Long?,
    onFilterStateChanged: (ClosedFloatingPointRange<Float>, Set<InvoiceStatus>) -> Unit,
    onApplyFilters: (InvoiceFilter) -> Unit,
    onClearFilters: () -> Unit,
    onBack: () -> Unit
) {
    var selectedFromDate by rememberSaveable { mutableStateOf(currentFilter.dateFrom) }
    var selectedToDate by rememberSaveable { mutableStateOf(currentFilter.dateTo) }

    // Si no hay rango seleccionado, coge los límites
    var sliderPosition by rememberSaveable(stateSaver = RangeSaver) {
        mutableStateOf(currentFilter.amountRange ?: amountBounds)
    }

    var selectedStatuses by rememberSaveable(stateSaver = StatusSetSaver) {
        mutableStateOf(currentFilter.statuses)
    }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    val statusOptions = remember { InvoiceStatus.all }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val filtrosEliminadosText = stringResource(R.string.filtros_eliminados)

    LaunchedEffect(sliderPosition, selectedStatuses.size) {
        onFilterStateChanged(sliderPosition, selectedStatuses.toSet())
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = DarkGray.copy(alpha = 0.9f),
                        contentColor = White
                    )
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(
                        start = Dimens.SpacingM,
                        end = Dimens.SpacingM,
                        top = 0.dp,
                        bottom = Dimens.SpacingS
                    )
                    .navigationBarsPadding()
            ) {

                Button(
                    onClick = {
                        onApplyFilters(
                            InvoiceFilter(
                                dateFrom = selectedFromDate,
                                dateTo = selectedToDate,
                                amountRange = sliderPosition,
                                statuses = selectedStatuses.toSet()
                            )
                        )
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
                ) {
                    Text(
                        stringResource(R.string.aplicar_filtros),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(Dimens.SpacingS))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            selectedFromDate = null
                            selectedToDate = null
                            sliderPosition = amountBounds
                            selectedStatuses = emptySet()

                            onClearFilters()

                            coroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(filtrosEliminadosText)
                            }
                        },
                        contentPadding = PaddingValues(
                            horizontal = Dimens.SpacingM,
                            vertical = Dimens.SpacingS
                        )
                    ) {
                        Text(
                            stringResource(R.string.borrar_filtros),
                            color = BrandGreen,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(Dimens.SpacingM)
        ) {
            // Cabecera Atrás
            Column(modifier = Modifier
                .padding()
                .padding(top = 20.dp, start = 0.dp, end = 0.dp, bottom = Dimens.SpacingS)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(vertical = Dimens.SpacingS),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .sizeIn(minWidth = Dimens.ButtonHeight, minHeight = Dimens.SpacingXL)
                            .clip(RoundedCornerShape(Dimens.CornerButton))
                            .clickable { onBack() }
                            .padding(horizontal = Dimens.SpacingS)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = BrandGreen,
                            modifier = Modifier.size(Dimens.IconXS)
                        )
                        Text(
                            text = stringResource(R.string.atras),
                            color = BrandGreen,
                            style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline)
                        )
                    }
                }
            }

            Text(stringResource(R.string.filtrar), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(Dimens.SpacingL))

            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()// Importante para no quedar debajo del bottomBar
                        .verticalScroll(rememberScrollState())
                ) {
                    // FILTRO POR FECHA
                    Text(
                        stringResource(R.string.por_fecha),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(Dimens.SpacingS))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        val isDateEnabled = minDateAllowed != null

                        // Refactorizado a Componente Clean & Reusable
                        DateFilterTextField(
                            label = if (isDateEnabled) stringResource(R.string.desde) else stringResource(R.string.sin_facturas),
                            selectedDate = selectedFromDate,
                            isEnabled = isDateEnabled,
                            isPickerOpen = showFromDatePicker,
                            onDateClick = { showFromDatePicker = true },
                            onClearClick = { selectedFromDate = null },
                            modifier = Modifier.weight(1f)
                        )

                        DateFilterTextField(
                            label = stringResource(R.string.hasta),
                            selectedDate = selectedToDate,
                            isEnabled = isDateEnabled,
                            isPickerOpen = showToDatePicker,
                            onDateClick = { showToDatePicker = true },
                            onClearClick = { selectedToDate = null },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(Dimens.SpacingXL))

                    // FILTRO POR IMPORTE
                    Text(
                        stringResource(R.string.por_un_importe),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(Dimens.SpacingS))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(color = BrandGreenLight, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                text = "${sliderPosition.start.formatSpanishInteger()} € - ${sliderPosition.endInclusive.formatSpanishInteger()} €",
                                color = TextMain,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // FILTRO POR IMPORTE
                        val minRangeGap = 1f
                        RangeSlider(
                            value = sliderPosition,
                            onValueChange = { newRange ->
                                // 2. Lógica de control de distancia
                                val start = newRange.start
                                val end = newRange.endInclusive

                                // Calculamos la distancia actual
                                val currentGap = end - start

                                if (currentGap < minRangeGap) {
                                    // Si el rango absoluto de las facturas permite tener 1€ de diferencia
                                    if (amountBounds.endInclusive - amountBounds.start >= minRangeGap) {

                                        // Si el usuario está moviendo el pulgar de la IZQUIERDA (mínimo)
                                        if (start != sliderPosition.start) {
                                            val fixedStart = (end - minRangeGap).coerceAtLeast(amountBounds.start)
                                            sliderPosition = fixedStart..end
                                        }
                                        // Si el usuario está moviendo el pulgar de la DERECHA (máximo)
                                        else {
                                            val fixedEnd = (start + minRangeGap).coerceAtMost(amountBounds.endInclusive)
                                            sliderPosition = start..fixedEnd
                                        }
                                    } else {
                                        // Caso borde: El total de facturas solo varía en menos de 1€ (ej. todas valen 13.50€)
                                        sliderPosition = newRange
                                    }
                                } else {
                                    // Distancia correcta, permitimos el cambio
                                    sliderPosition = newRange
                                }
                            },
                            valueRange = amountBounds,
                            colors = SliderDefaults.colors(
                                thumbColor = BrandGreen,
                                activeTrackColor = BrandGreen,
                                inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                            ),
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
                            Text(
                                text = "${amountBounds.start.formatSpanishInteger()} €",
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${amountBounds.endInclusive.formatSpanishInteger()} €",
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens.SpacingXL))

                    // FILTRO POR ESTADO
                    Text(
                        stringResource(R.string.por_estado),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(Dimens.SpacingS))

                    statusOptions.forEach { status ->
                        val ui = status.toUiModel(usePlural = true)
                        val isChecked = selectedStatuses.contains(status)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Dimens.CornerButtonXL))
                                .clickable {
                                    selectedStatuses =
                                        if (isChecked) selectedStatuses - status else selectedStatuses + status
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedStatuses.contains(status),
                                onCheckedChange = { checked ->
                                    selectedStatuses = if (checked) selectedStatuses + status else selectedStatuses - status
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BrandGreen,
                                    uncheckedColor = BrandGreen
                                ),
                                modifier = Modifier.scale(1.2f),
                            )
                            Text(
                                text = ui.label,
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(Modifier.height(Dimens.SpacingXXL))
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, White.copy(alpha = 0.7f))
                            )
                        )
                )
            }
            Spacer(Modifier.height(padding.calculateBottomPadding()))
        }
    }

    // Date Picker "Desde"
    if (showFromDatePicker) {
        DatePickerModal(
            initialDate = selectedFromDate,
            minDateMillis = minDateAllowed,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterTextField(
    label: String,
    selectedDate: String?,
    isEnabled: Boolean,
    isPickerOpen: Boolean,
    onDateClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Box(modifier = modifier) {
        TextField(
            value = if (isEnabled) (selectedDate ?: "") else "",
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                lineHeight = 1.sp
            ),
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            enabled = isEnabled,
                            onClick = {
                                if (selectedDate != null) onClearClick() else onDateClick()
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedDate != null && isEnabled) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = stringResource(R.string.borrar_fecha),
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = if (isEnabled) Color.Gray else Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .drawBehind {
                    val borderColor = when {
                        !isEnabled -> Color.LightGray.copy(alpha = 0.3f)
                        isPickerOpen -> BrandGreen
                        else -> Color.Gray
                    }
                    val strokeDp = if (isPickerOpen && isEnabled) 2.dp else 1.dp
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
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledLabelColor = Color.LightGray,
                focusedLabelColor = BrandGreen,
                unfocusedLabelColor = Color.Gray
            ),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(end = 48.dp)
                .clickable(
                    enabled = isEnabled,
                    onClick = onDateClick
                )
        )
    }
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
    val selectionMillis = initialDate.toEpochMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectionMillis,
        initialDisplayedMonthMillis = selectionMillis ?: minDateMillis ?: System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val isBeforeMax = maxDateMillis?.let { utcTimeMillis <= it } ?: true
                val isAfterMin = minDateMillis?.let { utcTimeMillis >= it } ?: true
                return isBeforeMax && isAfterMin
            }

            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = java.time.LocalDate.now().year
                val minYear = minDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).year
                } ?: 1900
                return year in minYear..currentYear
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(java.time.ZoneOffset.UTC).toLocalDate()
                    onDateSelected(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                }
            }) { Text(stringResource(R.string.ok), color = BrandGreen, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar), color = BrandGreen)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                todayContentColor = BrandGreen,
                todayDateBorderColor = BrandGreen,
                selectedDayContainerColor = BrandGreen,
                selectedDayContentColor = Color.White,
                disabledDayContentColor = Color(0xFFC4C4C4),
            )
        )
    }
}

val RangeSaver = listSaver<ClosedFloatingPointRange<Float>, Float>(
    save = { listOf(it.start, it.endInclusive) },
    restore = { it[0]..it[1] }
)

val StatusSetSaver = listSaver(
    save = { it.map { status -> status.id } },
    restore = { it.map { id -> InvoiceStatus.fromId(id) }.toSet() }
)