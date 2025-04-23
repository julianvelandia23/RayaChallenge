package org.julianvelandia.raya.presentation.conversion


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.julianvelandia.raya.domain.parseAmount
import org.julianvelandia.raya.presentation.dimens16dp
import org.julianvelandia.raya.presentation.dimens16sp
import org.julianvelandia.raya.presentation.dimens24dp
import org.julianvelandia.raya.presentation.dimens50dp
import org.julianvelandia.raya.presentation.dimens8dp
import org.julianvelandia.raya.presentation.formatCurrency
import org.koin.compose.koinInject
import rayachallenge.composeapp.generated.resources.Res
import rayachallenge.composeapp.generated.resources.amount_placeholder
import rayachallenge.composeapp.generated.resources.content_description_change_currency
import rayachallenge.composeapp.generated.resources.conversion_title
import rayachallenge.composeapp.generated.resources.exchange_rate_display
import rayachallenge.composeapp.generated.resources.from_label
import rayachallenge.composeapp.generated.resources.label_amount_receive
import rayachallenge.composeapp.generated.resources.label_button_continue
import rayachallenge.composeapp.generated.resources.loading_rate_message
import rayachallenge.composeapp.generated.resources.to_label


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConversionViewModel = koinInject(),
) {

    val state by viewModel.state.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var bottomSheetContentState by remember { mutableStateOf<BottomSheetState>(BottomSheetState.Hidden) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.eventResult.collectLatest { event ->
            when (event) {
                is Event.Success -> {
                    viewModel.onAmountChange("")
                    bottomSheetContentState = BottomSheetState.ShowingResult(true)
                }
                is Event.Error -> {
                    bottomSheetContentState = BottomSheetState.ShowingResult(false)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens16dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.conversion_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(dimens24dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CurrencySelector(
                label = stringResource(Res.string.from_label),
                selectedCurrency = state.selectedOriginCurrency,
                availableCurrencies = state.availableCurrencies,
                onCurrencySelected = { viewModel.onOriginCurrencyChange(it) },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.onSwapCurrencies() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(Res.string.content_description_change_currency)
                )
            }
            CurrencySelector(
                label = stringResource(Res.string.to_label),
                selectedCurrency = state.selectedDestCurrency,
                availableCurrencies = state.availableCurrencies,
                onCurrencySelected = { viewModel.onDestCurrencyChange(it) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(dimens24dp))

        OutlinedTextField(
            value = state.amountInputText,
            onValueChange = { newTextValue ->
                viewModel.onAmountChange(newTextValue)
            },
            label = { Text(stringResource(Res.string.amount_placeholder)) },
            leadingIcon = {
                Text(
                    state.selectedOriginCurrency,
                    fontSize = dimens16sp,
                    modifier = Modifier.padding(start = dimens8dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = state.rateError != null
        )
        Spacer(modifier = Modifier.height(dimens16dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(
                text = when {
                    state.isLoadingRate -> stringResource(Res.string.loading_rate_message)
                    state.rateError != null -> state.rateError.orEmpty()
                    state.exchangeRate != null -> stringResource(
                        Res.string.exchange_rate_display,
                        state.selectedOriginCurrency,
                        formatCurrency(state.exchangeRate, state.selectedDestCurrency)
                    )

                    else -> ""
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (state.rateError != null) MaterialTheme.colorScheme.error else LocalContentColor.current
            )
            Spacer(modifier = Modifier.height(dimens8dp))
            Text(
                text = stringResource(
                    Res.string.label_amount_receive,
                    formatCurrency(state.calculatedResult, state.selectedDestCurrency)
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val amountParsed = parseAmount(state.amountInputText)
                val currentRate = state.exchangeRate
                val currentResult = state.calculatedResult
                val originCurrency = state.selectedOriginCurrency
                val destCurrency = state.selectedDestCurrency

                if (amountParsed != null && currentResult != null && currentRate != null) {
                    val amountFromStr = formatCurrency(amountParsed, originCurrency)
                    val amountToStr = formatCurrency(currentResult, destCurrency)

                    val rateDisplayStr = formatCurrency(currentRate, destCurrency)

                    bottomSheetContentState = BottomSheetState.ShowingConfirmation(
                        amountFrom = amountFromStr,
                        currencyFrom = originCurrency,
                        amountTo = amountToStr,
                        currencyTo = destCurrency,
                        rate = "1 $originCurrency = $rateDisplayStr"
                    )
                    showSheet = true
                }
            },

            enabled = state.calculatedResult?.let { it > 0.0 } ?: false &&
                    !state.isLoadingRate &&
                    state.rateError == null,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens50dp)
        ) {
            Text(
                stringResource(Res.string.label_button_continue),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            BottomSheetContent(
                state = bottomSheetContentState,
                onConfirm = {
                    viewModel.performConversion()
                },
                onDismiss = {
                    showSheet = false
                }
            )
        }
    }
}



