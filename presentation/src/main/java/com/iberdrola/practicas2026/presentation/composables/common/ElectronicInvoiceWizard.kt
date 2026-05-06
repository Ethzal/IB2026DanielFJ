package com.iberdrola.practicas2026.presentation.composables.common

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.core.utils.getEmailError
import com.iberdrola.practicas2026.core.utils.maskEmail
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.mapper.toUiModel
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.ElectronicInvoiceViewModel
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.WizardEffect
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.WizardEvent
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.WizardState
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.WizardStep
import com.iberdrola.practicas2026.presentation.ui.theme.BgInfo
import com.iberdrola.practicas2026.presentation.ui.theme.BgInfoDisable
import com.iberdrola.practicas2026.presentation.ui.theme.BgLoading
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import com.iberdrola.practicas2026.presentation.ui.theme.WarningOrange
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import kotlinx.coroutines.launch

sealed class ContractStatus {
    data object Active : ContractStatus()
    data object Inactive : ContractStatus()
}

@Composable
fun WizardHeader(
    title: String,
    progress: Float? = null,
    showProgress: Boolean = false,
    interactionEnabled: Boolean = true,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingHeader)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.SpacingM)
                .height(Dimens.IconL)
        ) {
            if (showProgress) {
                IconButton(
                    onClick = onClose,
                    enabled = interactionEnabled,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = BrandGreen
                    )
                }
            } else {
                AppBackRow(onBack = onBack)
            }
        }

        Spacer(Modifier.height(Dimens.SpacingXS))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = Dimens.SpacingM)
        )

        if (showProgress && progress != null) {
            Spacer(Modifier.height(Dimens.SpacingS))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ProgressBarHeight)
                    .background(BrandGreen.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(BrandGreen)
                )
            }
        }
    }
}

@Composable
fun ContractListScreen(
    progress: Float,
    onActiveContractClick: () -> Unit,
    onInactiveContractClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { WizardHeader(title = stringResource(R.string.factura_electronica), onBack = onBack, progress = progress, onClose = {}) },
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Spacer(Modifier.height(Dimens.SpacingS))

            ContractRow(
                icon = R.drawable.ic_lightbulb,
                title = stringResource(R.string.contrato_de_luz),
                status = ContractStatus.Active,
                onClick = onActiveContractClick
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingM)) { AppDivider() }

            ContractRow(
                icon = R.drawable.ic_gas,
                title = stringResource(R.string.contrato_de_gas),
                status = ContractStatus.Inactive,
                onClick = onInactiveContractClick
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingM)) { AppDivider() }
        }
    }
}

@Composable
fun ContractRow(icon: Int, title: String, status: ContractStatus, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Dimens.SpacingM),
        verticalAlignment = Alignment.Top
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = BrandGreen, modifier = Modifier.size(Dimens.SpacingXL))
        Spacer(Modifier.width(Dimens.SpacingS))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = TextMain)
            Spacer(Modifier.height(Dimens.SpacingS))
            StatusPill(model = status.toUiModel())
        }
        Icon(painter = painterResource(id = R.drawable.ic_arrow_info), contentDescription = null, tint = TextSecondary, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun ModifyEmailInfoScreen(onModifyClick: () -> Unit, onBack: () -> Unit, email: String) {
    Scaffold(
        topBar = { WizardHeader(title = stringResource(R.string.contrato_de_luz), onBack = onBack, onClose = {}) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpacingM)
        ) {
            Text(stringResource(R.string.direccion2).lowercase(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(Dimens.SpacingL))
            Text(stringResource(R.string.actualmente_recibes), color = TextMain, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(Dimens.SpacingXL))
            Text(stringResource(R.string.recibes_facturas_en), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(Dimens.SpacingS))
            Text(text = email.ifEmpty { stringResource(R.string.email_test) }, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(Dimens.SpacingM))
            AppDivider()
            Spacer(Modifier.height(Dimens.SpacingL))
            Spacer(Modifier.height(Dimens.SpacingXS))

            Row {
                Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = TextSecondary, modifier = Modifier.size(Dimens.IconXXS))
                Spacer(Modifier.width(Dimens.SpacingS))
                Text(stringResource(R.string.recuerda_requisito), color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Normal)
            }

            Spacer(Modifier.weight(1f))

            WizardBottomBar(
                primaryText = stringResource(R.string.modificar_email),
                primaryIcon = R.drawable.ic_pencil,
                onPrimaryClick = onModifyClick,
                addGhostSpace = false
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputScreen(
    state: WizardState,
    progress: Float,
    onEmailChange: (String) -> Unit,
    onLegalChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    val title = if (state.isActivation) stringResource(R.string.activa_tu_factura) else stringResource(R.string.modificar_email)
    
    // Gestión de errores VISUALES (Se queda en la UI)
    var showErrors by remember { mutableStateOf(false) }
    val emailError = if (showErrors || state.draftEmail.isNotEmpty()) {
        if (showErrors && state.draftEmail.isEmpty()) stringResource(R.string.error_email_vacio)
        else getEmailError(state.draftEmail)
    } else null
    val legalError = showErrors && state.isActivation && !state.isLegalChecked
    
    val canAdvance = if (state.isActivation) state.isEmailValid && state.isLegalChecked else state.isEmailValid

    val emailShakeOffset = rememberShakeOffset()
    val legalShakeOffset = rememberShakeOffset()
    val scope = rememberCoroutineScope()
    var isFocused by remember { mutableStateOf(false) }
    var showNotAvailableDialog by remember { mutableStateOf(false) }

    if (showNotAvailableDialog) {
        AlertDialog(
            onDismissRequest = { showNotAvailableDialog = false },
            title = { Text(stringResource(R.string.informacion)) },
            text = { Text(stringResource(R.string.no_disponible), fontWeight = FontWeight.Normal) },
            confirmButton = {
                TextButton(onClick = { showNotAvailableDialog = false }) {
                    Text(stringResource(R.string.aceptar), color = BrandGreen)
                }
            },
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = { WizardHeader(title = title, progress = progress, onBack = onBack, showProgress = true, onClose = onClose) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpacingM)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isActivation) {
                Spacer(Modifier.height(Dimens.SpacingS))
                Text(stringResource(R.string.email_vinculado), color = TextSecondary, style = MaterialTheme.typography.bodySmall, fontSize = Dimens.TextS)
                Spacer(Modifier.height(Dimens.SpacingXS))
                Text(text = stringResource(R.string.email_test).maskEmail(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(Dimens.SpacingL))
            }

            Spacer(Modifier.height(Dimens.SpacingXS))
            Text(stringResource(R.string.en_que_email_deseas), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(Dimens.SpacingS))
            Spacer(Modifier.height(Dimens.SpacingXS))

            TextField(
                value = state.draftEmail,
                onValueChange = {
                    onEmailChange(it)
                    if (showErrors) showErrors = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.TextFieldHeight)
                    .offset(x = emailShakeOffset.value.dp)
                    .onFocusChanged { isFocused = it.isFocused }
                    .drawBehind {
                        val strokeWidth = if (isFocused) Dimens.StrokeDefault.toPx() else Dimens.StrokeThick.toPx()
                        val color =
                            if (emailError != null) Color.Red else if (isFocused) BrandGreen else TextSecondary
                        drawLine(
                            color = color,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                isError = emailError != null,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                label = { Text(text = if (state.isActivation) stringResource(R.string.label_email) else stringResource(
                    R.string.label_nuevo_email
                ), color = if (emailError != null) Color.Red else TextSecondary, fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent, errorContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent, focusedLabelColor = BrandGreen, cursorColor = BrandGreen
                ),
                singleLine = true
            )

            Box(modifier = Modifier
                .height(Dimens.SpacingM)
                .padding(start = Dimens.SpacingXS, top = Dimens.SpacingXS)) {
                if (emailError != null) {
                    Text(emailError, color = Color.Red, style = MaterialTheme.typography.labelSmall)
                }
            }

            if (state.isActivation) {
                Spacer(Modifier.height(Dimens.SpacingM))
                LegalInfoBlock(onInfoClick = { showNotAvailableDialog = true })
                Spacer(Modifier.height(Dimens.SpacingL))

                Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = state.isLegalChecked,
                        onCheckedChange = {
                            onLegalChange(it)
                            if (showErrors) showErrors = false
                        },
                        modifier = Modifier
                            .scale(1.2f)
                            .offset(x = legalShakeOffset.value.dp),
                        colors = CheckboxDefaults.colors(checkedColor = BrandGreen, uncheckedColor = if (legalError) Color.Red else BrandGreen)
                    )

                    val checkboxAnnotatedString = buildAnnotatedString {
                        append(stringResource(R.string.he_leido_y_acepto) + " ")
                        pushLink(LinkAnnotation.Clickable(tag = "conditions", linkInteractionListener = LinkInteractionListener { showNotAvailableDialog = true }))
                        withStyle(style = SpanStyle(color = BrandGreen, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.condiciones_generales))
                        }
                        pop()
                        append(" " + stringResource(R.string.he_leido_y_acepto_2))
                    }

                    BasicText(
                        text = checkboxAnnotatedString,
                        style = MaterialTheme.typography.bodyLarge.copy(color = TextMain, fontWeight = FontWeight.Normal),
                        modifier = Modifier.padding(start = Dimens.SpacingXS, top = Dimens.SpacingXM)
                    )
                }
            }

            if (legalError) {
                Box(modifier = Modifier
                    .padding(start = Dimens.SpacingXXXL, top = Dimens.SpacingXS)
                    .height(Dimens.SpacingL)) {
                    Text(text = stringResource(R.string.error_legal_requerido), color = Color.Red, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(Modifier.weight(1f))

            WizardBottomBar(
                primaryText = stringResource(R.string.siguiente),
                onPrimaryClick = {
                    if (canAdvance) {
                        onSubmit()
                    } else {
                        showErrors = true
                        if (!state.isEmailValid) scope.launch { emailShakeOffset.shake() }
                        if (state.isActivation && !state.isLegalChecked) scope.launch { legalShakeOffset.shake() }
                    }
                },
                primaryEnabled = canAdvance,
                secondaryText = stringResource(R.string.anterior),
                onSecondaryClick = onBack,
                addGhostSpace = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    state: WizardState,
    progress: Float,
    onOtpChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit,
    onCloseNotice: () -> Unit
) {
    val title = if (state.isActivation) stringResource(R.string.activa_tu_factura) else stringResource(R.string.modificar_email)
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }
    val otpShakeOffset = rememberShakeOffset()
    val scope = rememberCoroutineScope()
    
    var showErrors by remember { mutableStateOf(false) }
    val otpError = if (showErrors && !state.isOtpValid) stringResource(R.string.error_otp_incompleto) else null

    val attemptsText = remember(state.otpAttemptsLeft) { getAttemptsRemainingText(context, state.otpAttemptsLeft) }
    var showNotAvailableDialog by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (showNotAvailableDialog) {
        AlertDialog(
            onDismissRequest = { showNotAvailableDialog = false },
            title = { Text(stringResource(R.string.informacion)) },
            text = { Text(stringResource(R.string.no_disponible), fontWeight = FontWeight.Normal) },
            confirmButton = { TextButton(onClick = { showNotAvailableDialog = false }) { Text(stringResource(R.string.aceptar), color = BrandGreen) } },
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            WizardHeader(
                title = title,
                progress = progress,
                onBack = onBack,
                showProgress = true,
                interactionEnabled = !state.isLoading,
                onClose = onClose
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpacingM)
        ) {
            Text(stringResource(R.string.introduce_codigo), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(Dimens.SpacingM))
            Text(stringResource(R.string.para_verificar_identidad), color = TextMain, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(Dimens.SpacingM))

            TextField(
                value = state.otpCode,
                onValueChange = {
                    onOtpChange(it)
                    if (showErrors && it.length == 6) showErrors = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.SpacingXXXXL)
                    .offset(x = otpShakeOffset.value.dp)
                    .onFocusChanged { isFocused = it.isFocused }
                    .drawBehind {
                        val strokeWidth =
                            if (isFocused) Dimens.StrokeThick.toPx() else Dimens.StrokeDefault.toPx()
                        val color =
                            if (otpError != null) Color.Red else if (isFocused) BrandGreen else TextSecondary
                        drawLine(
                            color = color,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                label = { Text(text = stringResource(R.string.codigo_verificacion), color = if (otpError != null) Color.Red else TextSecondary, fontWeight = FontWeight.Normal) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent, errorIndicatorColor = Color.Transparent,
                    focusedLabelColor = BrandGreen, cursorColor = BrandGreen
                ),
                singleLine = true
            )

            Box(modifier = Modifier
                .height(Dimens.SpacingL)
                .padding(start = Dimens.SpacingXS, top = Dimens.SpacingXS)) {
                if (otpError != null) {
                    Text(otpError, color = Color.Red, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(Modifier.height(Dimens.SpacingS))

            val boxColor = if (state.verSoporte) BgInfoDisable else BgInfo

            Surface(color = boxColor, shape = RoundedCornerShape(topEnd = Dimens.SpacingM, bottomEnd = Dimens.SpacingM, bottomStart = Dimens.SpacingM)) {
                Row(modifier = Modifier
                    .padding(Dimens.SpacingM)
                    .fillMaxWidth()) {
                    Icon(painterResource(R.drawable.ic_info), contentDescription = null, tint = if (state.verSoporte) WarningOrange else TextSecondary)
                    Spacer(Modifier.width(Dimens.SpacingS))
                    Column {
                        if (!state.verSoporte) {
                            Text(stringResource(R.string.no_has_recibido), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(Dimens.SpacingXS))
                            Text(text = stringResource(R.string.si_no_lo_encuentras), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = Dimens.SpacingXS))
                            if (state.hasRequestedResend) {
                                Text(text = attemptsText, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = Dimens.SpacingXS))
                            }
                            Text(
                                text = stringResource(R.string.volver_a_enviar),
                                color = if (state.otpAttemptsLeft > 0) BrandGreen else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                                modifier = Modifier
                                    .clickable { onResendClick() }
                                    .padding(top = Dimens.SpacingS)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.otp_limite_titulo),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(Modifier.height(Dimens.SpacingXS))

                            Text(
                                text = stringResource(R.string.otp_limite_descripcion),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = Dimens.SpacingXS)
                            )

                            Text(
                                text = stringResource(R.string.otp_llamar_atencion_cliente),
                                color = WarningOrange,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textDecoration = TextDecoration.Underline
                                ),
                                modifier = Modifier
                                    .clickable { showNotAvailableDialog = true }
                                    .padding(top = Dimens.SpacingS)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            if (state.otpResendState == 2) {
                InlineNotice(message = stringResource(R.string.hemos_vuelto_enviar), onClose = onCloseNotice)
            } else if (state.otpResendState == 3) {
                InlineNotice(message = stringResource(R.string.error_intentos_agotados), isError = true, onClose = onCloseNotice)
            }

            WizardBottomBar(
                primaryText = stringResource(R.string.siguiente),
                onPrimaryClick = {
                    if (!state.isLoading) {
                        if (state.isOtpValid) {
                            onSubmit()
                        } else {
                            showErrors = true
                            scope.launch { otpShakeOffset.shake() }
                        }
                    }
                },
                secondaryText = stringResource(R.string.anterior),
                onSecondaryClick = onBack,
                secondaryEnabled = !state.isLoading,
                interactionEnabled = !state.isLoading,
                primaryEnabled = state.isOtpValid && !state.isLoading,
                addGhostSpace = true
            )
        }
    }
}

@Composable
fun SuccessScreen(isActivation: Boolean, displayEmail: String, onAccept: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BrandGreen)) {
        IconButton(onClick = onAccept, modifier = Modifier
            .align(Alignment.TopEnd)
            .statusBarsPadding()
            .padding(vertical = Dimens.SpacingM)) {
            Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = null, tint = Color.White, modifier = Modifier.size(Dimens.IconS))
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpacingXL)
            .navigationBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.ic_thumbs_up), contentDescription = null, modifier = Modifier.size(Dimens.SuccessIconSize), tint = Color.Unspecified)
            Spacer(Modifier.height(Dimens.SpacingXL))
            Text(text = if (isActivation) stringResource(R.string.has_activado_correctamente) else stringResource(R.string.has_modificado_correctamente), color = Color.White, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(Dimens.SpacingL))
            Text(text = stringResource(R.string.pronto_recibiras_correo) + " " + displayEmail, color = Color.White, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Normal)
            Spacer(Modifier.weight(1f))
            Button(onClick = onAccept, modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(Dimens.CornerButtonXL)) {
                Text(text = stringResource(R.string.aceptar), color = BrandGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WizardContainer(
    viewModel: ElectronicInvoiceViewModel = hiltViewModel(),
    onExit: () -> Unit
) {
    val view = LocalView.current
    val activity = view.context as Activity
    val window = activity.window
    val controller = remember(window, view) { WindowCompat.getInsetsController(window, view) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    val targetProgress = when (state.step) {
        WizardStep.CONTRACT_LIST -> 0.0f
        WizardStep.MODIFY_INFO -> 0.0f
        WizardStep.EMAIL_INPUT -> 0.5f
        WizardStep.OTP -> 0.75f
        WizardStep.SUCCESS -> 1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
        label = stringResource(R.string.progress)
    )

    // Efecto de Navegación de Salida
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WizardEffect.ExitWizard -> onExit()
            }
        }
    }

    // Efecto para la barra de estado (Loader)
    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        } else {
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }
    }

    BackHandler(enabled = !state.isLoading) {
        viewModel.onEvent(WizardEvent.NavigateBack)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when (state.step) {
            WizardStep.CONTRACT_LIST -> ContractListScreen(
                progress = animatedProgress,
                onActiveContractClick = { viewModel.onEvent(WizardEvent.SelectActiveContract) },
                onInactiveContractClick = { viewModel.onEvent(WizardEvent.SelectInactiveContract) },
                onBack = { viewModel.onEvent(WizardEvent.NavigateBack) }
            )

            WizardStep.MODIFY_INFO -> ModifyEmailInfoScreen(
                email = state.lightContractEmail,
                onModifyClick = { viewModel.onEvent(WizardEvent.GoToEmailInput) },
                onBack = { viewModel.onEvent(WizardEvent.NavigateBack) }
            )

            WizardStep.EMAIL_INPUT -> EmailInputScreen(
                state = state,
                progress = animatedProgress,
                onEmailChange = { viewModel.onEvent(WizardEvent.UpdateEmail(it)) },
                onLegalChange = { viewModel.onEvent(WizardEvent.UpdateLegal(it)) },
                onSubmit = { viewModel.onEvent(WizardEvent.SubmitEmail) },
                onBack = { viewModel.onEvent(WizardEvent.NavigateBack) },
                onClose = { viewModel.onEvent(WizardEvent.CloseWizard) }
            )

            WizardStep.OTP -> OtpVerificationScreen(
                state = state,
                progress = animatedProgress,
                onOtpChange = { viewModel.onEvent(WizardEvent.UpdateOtp(it)) },
                onResendClick = { viewModel.onEvent(WizardEvent.ResendOtp) },
                onSubmit = { viewModel.onEvent(WizardEvent.SubmitOtp) },
                onBack = { viewModel.onEvent(WizardEvent.NavigateBack) },
                onClose = { viewModel.onEvent(WizardEvent.CloseWizard) },
                onCloseNotice = { viewModel.onEvent(WizardEvent.DismissNotice) }
            )

            WizardStep.SUCCESS -> SuccessScreen(
                isActivation = state.isActivation,
                displayEmail = state.draftEmail.maskEmail(),
                onAccept = { viewModel.onEvent(WizardEvent.AcceptSuccess) }
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(BgLoading),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.LoadingIndicatorSize),
                    color = BrandGreen,
                    trackColor = Color.White.copy(alpha = 0.2f),
                    strokeWidth = Dimens.LoadingStrokeWidth
                )
            }
        }
    }
}

fun getAttemptsRemainingText(context: Context, attempts: Int): String {
    return context.resources.getQuantityString(R.plurals.intentos_restantes, attempts, attempts)
}