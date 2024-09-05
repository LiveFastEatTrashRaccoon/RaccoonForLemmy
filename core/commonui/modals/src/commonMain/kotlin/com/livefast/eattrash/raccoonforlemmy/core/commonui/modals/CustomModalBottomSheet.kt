package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class CustomModalBottomSheetItem(
    val leadingContent: @Composable (() -> Unit)? = null,
    val label: String,
    val subtitle: String? = null,
    val customLabelStyle: TextStyle? = null,
    val trailingContent: @Composable (() -> Unit)? = null,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CustomModalBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: String = "",
    items: List<CustomModalBottomSheetItem> = emptyList(),
    onSelected: ((Int?) -> Unit)? = null,
    onLongPress: ((Int) -> Unit)? = null,
) {
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(ancillaryTextAlpha)

    ModalBottomSheet(
        sheetState = sheetState,
        windowInsets = WindowInsets(0, 0, 0, 0),
        onDismissRequest = {
            onSelected?.invoke(null)
        },
        content = {
            Column(
                modifier = Modifier.padding(bottom = Spacing.xl),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = fullColor,
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                LazyColumn {
                    itemsIndexed(items = items) { idx, item ->
                        Surface(
                            shape = RoundedCornerShape(CornerSize.xl),
                        ) {
                            Row(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = {
                                            sheetScope.launch {
                                                sheetState.hide()
                                            }.invokeOnCompletion {
                                                onSelected?.invoke(idx)
                                            }
                                        },
                                        onLongClick = {
                                            sheetScope.launch {
                                                sheetState.hide()
                                            }.invokeOnCompletion {
                                                onLongPress?.invoke(idx)
                                            }
                                        },
                                    ).padding(
                                        horizontal = Spacing.m,
                                        vertical = Spacing.s,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            ) {
                                item.leadingContent?.invoke()
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                                ) {
                                    Text(
                                        text = item.label,
                                        style =
                                        item.customLabelStyle
                                            ?: MaterialTheme.typography.bodyLarge,
                                        color = fullColor,
                                    )
                                    if (!item.subtitle.isNullOrEmpty()) {
                                        Text(
                                            text = item.subtitle,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = ancillaryColor,
                                        )
                                    }
                                }
                                item.trailingContent?.invoke()
                            }
                        }
                    }
                }
            }
        },
    )
}