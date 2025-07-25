package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderBottomSheet(
    title: String,
    min: Float,
    max: Float,
    initial: Float,
    modifier: Modifier = Modifier,
    state: SheetState = rememberModalBottomSheetState(),
    onSelect: ((Float?) -> Unit)? = null,
    sheetScope: CoroutineScope = rememberCoroutineScope(),
) {
    var value by remember {
        mutableFloatStateOf(initial)
    }

    ModalBottomSheet(
        modifier = modifier,
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = state,
        onDismissRequest = {
            onSelect?.invoke(null)
        },
    ) {
        Column(
            modifier = Modifier.padding(bottom = Spacing.xs),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.xs))

            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                valueRange = min.rangeTo(max),
                onValueChange = {
                    value = it
                },
            )

            Spacer(modifier = Modifier.height(Spacing.s))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    sheetScope
                        .launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onSelect?.invoke(value)
                        }
                },
            ) {
                Text(text = LocalStrings.current.buttonConfirm)
            }
        }
    }
}
