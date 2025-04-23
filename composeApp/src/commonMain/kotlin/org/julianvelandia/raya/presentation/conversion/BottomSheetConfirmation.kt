package org.julianvelandia.raya.presentation.conversion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.julianvelandia.raya.presentation.dimens16dp
import org.julianvelandia.raya.presentation.dimens24dp
import org.julianvelandia.raya.presentation.dimens8dp
import rayachallenge.composeapp.generated.resources.Res
import rayachallenge.composeapp.generated.resources.bottom_sheet_cancel_button
import rayachallenge.composeapp.generated.resources.bottom_sheet_confirm_button
import rayachallenge.composeapp.generated.resources.bottom_sheet_confirm_title
import rayachallenge.composeapp.generated.resources.bottom_sheet_convert_label
import rayachallenge.composeapp.generated.resources.bottom_sheet_rate_label
import rayachallenge.composeapp.generated.resources.bottom_sheet_receive_label

@Composable
fun BottomSheetConfirmation(
    state: BottomSheetState.ShowingConfirmation,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Text(
        text = stringResource(Res.string.bottom_sheet_confirm_title),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(dimens16dp))
    Text(
        text = stringResource(Res.string.bottom_sheet_convert_label),
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        text = state.amountFrom,
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(dimens8dp))
    Text(
        text = stringResource(Res.string.bottom_sheet_receive_label),
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        text = state.amountTo,
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(dimens8dp))

    Text(
        text = stringResource(Res.string.bottom_sheet_rate_label, state.rate),
        style = MaterialTheme.typography.bodySmall
    )
    Spacer(modifier = Modifier.height(dimens24dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(onClick = onDismiss) {
            Text(stringResource(Res.string.bottom_sheet_cancel_button))
        }
        Button(onClick = onConfirm) {
            Text(stringResource(Res.string.bottom_sheet_confirm_button))
        }
    }
    Spacer(modifier = Modifier.height(dimens16dp))
}