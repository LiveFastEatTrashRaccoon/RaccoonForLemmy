package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed interface SelectNumberBottomSheetType {
    data object PostBodyMaxLines : SelectNumberBottomSheetType

    data object InboxPreviewMaxLines : SelectNumberBottomSheetType
}

@Composable
private fun SelectNumberBottomSheetType.toReadableTitle() =
    when (this) {
        SelectNumberBottomSheetType.InboxPreviewMaxLines -> LocalStrings.current.settingsInboxPreviewMaxLines
        SelectNumberBottomSheetType.PostBodyMaxLines -> LocalStrings.current.settingsPostBodyMaxLines
    }

fun SelectNumberBottomSheetType.toInt() =
    when (this) {
        SelectNumberBottomSheetType.InboxPreviewMaxLines -> 1
        SelectNumberBottomSheetType.PostBodyMaxLines -> 0
    }

fun Int.toSelectNumberBottomSheetType(): SelectNumberBottomSheetType =
    when (this) {
        1 -> SelectNumberBottomSheetType.InboxPreviewMaxLines
        else -> SelectNumberBottomSheetType.PostBodyMaxLines
    }

private const val CUSTOM = -1
private const val UNLIMITED = -2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectNumberBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    state: SheetState = rememberModalBottomSheetState(),
    values: List<Int?> =
        listOf(
            1,
            10,
            50,
            CUSTOM,
            UNLIMITED,
        ),
    initialValue: Int?,
    type: SelectNumberBottomSheetType,
    onSelected: ((Int?) -> Unit)? = null,
) {
    var customDialogOpened by remember { mutableStateOf(false) }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = state,
        onDismissRequest = {
            onSelected?.invoke(null)
        },
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = type.toReadableTitle(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            LazyColumn {
                items(values) { value ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(CornerSize.xl))
                                .clickable {
                                    if (value == CUSTOM) {
                                        customDialogOpened = true
                                    } else {
                                        onSelected?.invoke(value)
                                    }
                                }.padding(
                                    horizontal = Spacing.m,
                                    vertical = Spacing.s,
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                    ) {
                        val text =
                            when (value) {
                                UNLIMITED -> LocalStrings.current.settingsPostBodyMaxLinesUnlimited
                                CUSTOM -> LocalStrings.current.settingsColorCustom
                                else -> value.toString()
                            }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }

        if (customDialogOpened) {
            NumberPickerDialog(
                title = LocalStrings.current.settingsColorCustom,
                initialValue = initialValue ?: 0,
                onClose = {
                    customDialogOpened = false
                },
                onSubmit = { value ->
                    sheetScope
                        .launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onSelected?.invoke(value)
                        }
                },
            )
        }
    }
}
