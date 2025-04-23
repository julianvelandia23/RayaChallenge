package org.julianvelandia.raya.presentation.conversion

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.julianvelandia.raya.presentation.dimens16dp
import org.julianvelandia.raya.presentation.dimens24dp
import org.julianvelandia.raya.presentation.dimens64dp
import rayachallenge.composeapp.generated.resources.Res
import rayachallenge.composeapp.generated.resources.status_error
import rayachallenge.composeapp.generated.resources.status_success
import rayachallenge.composeapp.generated.resources.label_close
import rayachallenge.composeapp.generated.resources.*

@Composable
fun BottomSheetResult(
    state: BottomSheetState.ShowingResult,
    onDismiss: () -> Unit,
) {
    Icon(
        imageVector = if (state.success) Icons.Filled.CheckCircle else Icons.Filled.Warning,
        contentDescription = if (state.success) stringResource(Res.string.status_success) else stringResource(
            Res.string.status_error
        ),
        tint = if (state.success) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
        modifier = Modifier.size(dimens64dp)
    )
    Spacer(modifier = Modifier.height(dimens16dp))
    Text(
        text = if (state.success) {
            stringResource(Res.string.conversion_success_message)
        } else {
            stringResource(Res.string.conversion_error_message)
        },
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(dimens24dp))
    Button(onClick = onDismiss) { Text(stringResource(Res.string.label_close)) }
    Spacer(modifier = Modifier.height(dimens16dp))
}