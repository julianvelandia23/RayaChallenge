package org.julianvelandia.raya.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.julianvelandia.raya.domain.Balance
import org.julianvelandia.raya.domain.Transaction
import org.koin.compose.koinInject
import rayachallenge.composeapp.generated.resources.Res
import rayachallenge.composeapp.generated.resources.conversion_value
import rayachallenge.composeapp.generated.resources.my_balances_title
import rayachallenge.composeapp.generated.resources.last_transactions_title
import rayachallenge.composeapp.generated.resources.empty_transactions
import rayachallenge.composeapp.generated.resources.create_transaction


@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = koinInject(),
    navigateTo: (route: String) -> Unit = {},
) {
    val balanceState by viewModel.balanceState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens8dp),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = dimens8dp)
            ) {
                SectionHeader(icon = "💰", title = stringResource(Res.string.my_balances_title))
                Spacer(modifier = Modifier.height(dimens8dp))
                Column(modifier = Modifier.padding(horizontal = dimens16dp)) {
                    balanceState.balances.forEach { balance ->
                        BalanceItem(balance = balance)
                    }
                }

                Spacer(modifier = Modifier.height(dimens16dp))

                SectionHeader(icon = "📄", title = stringResource(Res.string.last_transactions_title))
                Spacer(modifier = Modifier.height(dimens8dp))
                Column(modifier = Modifier.padding(horizontal = dimens16dp)) {
                    if (transactionState.transactions.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.empty_transactions),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = dimens8dp)
                                .fillMaxWidth()
                        )
                    } else {
                        transactionState.transactions.forEach { transaction ->
                            TransactionItem(transaction = transaction)
                            Spacer(modifier = Modifier.height(dimens4dp))
                        }
                    }
                }
            }

            Button(
                onClick = { navigateTo(Screen.Conversion.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimens16dp, end = dimens16dp, bottom = dimens16dp, top = dimens8dp) // Padding para el botón fijo
            ) {
                Text(
                    text = stringResource(Res.string.create_transaction),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}


@Composable
fun SectionHeader(icon: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens16dp, vertical = dimens8dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = dimens20sp)
        Spacer(modifier = Modifier.width(dimens8dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BalanceItem(balance: Balance) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = balance.flag,
            fontSize = 24.sp,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = balance.currencyCode,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCurrency(balance.amount, balance.currencyCode),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Text(
        text = stringResource(
            Res.string.conversion_value,
            formatCurrency(transaction.sourceAmount, transaction.sourceCurrencyCode),
            formatCurrency(transaction.targetAmount, transaction.targetCurrencyCode)
        ),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens8dp)
    )
}




