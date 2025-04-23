package org.julianvelandia.raya.presentation.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.julianvelandia.raya.domain.CalculateResultUseCase
import org.julianvelandia.raya.domain.ConversionError
import org.julianvelandia.raya.domain.GetConversionRateUseCase
import org.julianvelandia.raya.domain.PerformConversionUseCase
import org.julianvelandia.raya.domain.parseAmount
import org.julianvelandia.raya.presentation.isValidDecimalInput
import org.julianvelandia.raya.presentation.supportCurrencies

sealed class Event {
    data object Success : Event()
    data class Error(val message: String? = null) : Event()
}

data class ConversionScreenState(
    val amountInputText: String = "",
    val selectedOriginCurrency: String = supportCurrencies[1],
    val selectedDestCurrency: String = supportCurrencies[0],
    val exchangeRate: Double? = null,
    val calculatedResult: Double? = null,
    val isLoadingRate: Boolean = false,
    val rateError: String? = null,
    val availableCurrencies: List<String> = supportCurrencies
)

class ConversionViewModel(
    private val getConversionRateUseCase: GetConversionRateUseCase,
    private val performConversionUseCase: PerformConversionUseCase,
    private val calculateResultUseCase: CalculateResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConversionScreenState())
    val state: StateFlow<ConversionScreenState> = _state.asStateFlow()

    var eventResult = MutableSharedFlow<Event>()
        private set

    init {
        fetchExchangeRate()

        viewModelScope.launch {
            _state.map { Triple(it.amountInputText, it.exchangeRate, it.selectedOriginCurrency) }
                .distinctUntilChanged()
                .filter { (amountText, _, _) -> amountText.isNotBlank() }
                .collectLatest { (amountText, rate, _) ->
                    calculateResultUseCase(amountText, rate, _state.value.selectedOriginCurrency)
                        .onSuccess { result ->
                            _state.update { it.copy(calculatedResult = result, rateError = null) }
                        }
                        .onFailure { error ->
                            _state.update {
                                it.copy(
                                    calculatedResult = null,
                                    rateError = error.message
                                )
                            }
                        }
                }
        }
        viewModelScope.launch {
            _state.map { Pair(it.selectedOriginCurrency, it.selectedDestCurrency) }
                .distinctUntilChanged()
                .collect { (_, _) ->
                    fetchExchangeRate()
                }
        }
    }

    fun onAmountChange(newAmountText: String) {
        if (newAmountText.isValidDecimalInput()) {
            _state.update { it.copy(amountInputText = newAmountText) }
        }
    }


    fun onOriginCurrencyChange(newCurrency: String) {
        val currentDest = _state.value.selectedDestCurrency
        _state.update {
            it.copy(
                selectedOriginCurrency = newCurrency,
                selectedDestCurrency = if (newCurrency == currentDest) {
                    supportCurrencies.first { c -> c != newCurrency }
                } else {
                    currentDest
                }
            )
        }
    }

    fun onDestCurrencyChange(newCurrency: String) {
        val currentOrigin = _state.value.selectedOriginCurrency
        _state.update {
            it.copy(
                selectedDestCurrency = newCurrency,
                selectedOriginCurrency = if (newCurrency == currentOrigin) {
                    supportCurrencies.first { c -> c != newCurrency }
                } else {
                    currentOrigin
                }
            )
        }
    }

    fun onSwapCurrencies() {
        _state.update { currentState ->
            currentState.copy(
                selectedOriginCurrency = currentState.selectedDestCurrency,
                selectedDestCurrency = currentState.selectedOriginCurrency
            )
        }
    }

    private fun fetchExchangeRate() {
        val origin = _state.value.selectedOriginCurrency
        val dest = _state.value.selectedDestCurrency
        val amount = _state.value.amountInputText.toDoubleOrNull() ?: 0.0

        if (origin == dest) {
            _state.update {
                it.copy(
                    exchangeRate = 1.0,
                    rateError = null,
                    isLoadingRate = false
                )
            }
            return
        }

        _state.update {
            it.copy(
                isLoadingRate = true,
                rateError = null,
                exchangeRate = null,
                calculatedResult = null
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoadingRate = true) }
            getConversionRateUseCase(origin, dest, amount)
                .onSuccess { rate ->
                    _state.update {
                        it.copy(exchangeRate = rate, isLoadingRate = false)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(rateError = error.message, isLoadingRate = false)
                    }
                }
        }
    }


    fun performConversion() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = state.value

            val originAmount = parseAmount(currentState.amountInputText)
            val destAmount = currentState.calculatedResult

            if (originAmount == null || originAmount <= 0.0) {
                eventResult.emit(Event.Error(ConversionError.MountNotValid.message))
                return@launch
            }
            if (destAmount == null || destAmount <= 0.0) {
                eventResult.emit(Event.Error(ConversionError.ResultNotValid.message))
                return@launch
            }
            val originCode = currentState.selectedOriginCurrency
            val destCode = currentState.selectedDestCurrency

            try {
                performConversionUseCase.execute(
                    originCurrency = originCode,
                    originAmount = originAmount,
                    destCurrency = destCode,
                    destAmount = destAmount
                )
                eventResult.emit(Event.Success)

            } catch (e: Exception) {
                eventResult.emit(Event.Error(e.message))
            }
        }
    }

}