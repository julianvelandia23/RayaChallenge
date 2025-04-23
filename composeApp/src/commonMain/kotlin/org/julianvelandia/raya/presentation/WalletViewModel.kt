package org.julianvelandia.raya.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.julianvelandia.raya.domain.Balance
import org.julianvelandia.raya.domain.Transaction
import org.julianvelandia.raya.domain.WalletRepository


data class BalanceState(
    val isLoading: Boolean = true,
    val balances: List<Balance> = emptyList(),
    val error: String? = null
)

data class TransactionState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null
)

class WalletViewModel(
    private val walletRepository: WalletRepository
) : ViewModel() {

    val balanceState: StateFlow<BalanceState> = walletRepository.getBalance()
        .distinctUntilChanged()
        .map { result ->
            result.fold(
                onSuccess = { data ->
                    BalanceState(
                        isLoading = false,
                        balances = data,
                        error = result.exceptionOrNull()?.message
                    )
                },
                onFailure = {
                    BalanceState(
                        isLoading = false,
                        balances = emptyList(),
                        error = result.exceptionOrNull()?.message
                    )
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = BalanceState()
        )

    val transactionState: StateFlow<TransactionState> = walletRepository.getTransactions()
        .distinctUntilChanged()
        .map { result ->
            result.fold(
                onSuccess = { data ->
                    TransactionState(
                        isLoading = false,
                        transactions = data,
                        error = result.exceptionOrNull()?.message
                    )
                },
                onFailure = {
                    TransactionState(
                        isLoading = false,
                        transactions = emptyList(),
                        error = result.exceptionOrNull()?.message
                    )
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TransactionState()
        )

}
