package org.julianvelandia.raya.presentation.conversion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.julianvelandia.raya.presentation.dimens100dp
import org.julianvelandia.raya.presentation.dimens16dp
import org.julianvelandia.raya.presentation.dimens8dp

@Composable
fun BottomSheetContent(
    state: BottomSheetState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(horizontal = dimens16dp, vertical = dimens8dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (state) {
            is BottomSheetState.Hidden -> {
                Spacer(modifier = Modifier.height(dimens100dp))
            }

            is BottomSheetState.ShowingConfirmation -> {
                BottomSheetConfirmation(
                    state = state,
                    onConfirm = onConfirm,
                    onDismiss = onDismiss
                )
            }

            is BottomSheetState.ShowingResult -> {
                BottomSheetResult(
                    state = state,
                    onDismiss = onDismiss
                )
            }
        }
    }
}