package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing

data class CustomModalBottomSheetItem(
    val leadingContent: @Composable (() -> Unit)? = null,
    val label: String,
    val customLabelStyle: TextStyle? = null,
    val trailingContent: @Composable (() -> Unit)? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: String = "",
    items: List<CustomModalBottomSheetItem> = emptyList(),
    onSelected: ((Int?) -> Unit)? = null,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onSelected?.invoke(null)
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(
                        start = Spacing.m,
                        end = Spacing.m,
                        bottom = Spacing.xl
                    ),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
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
                                    .clickable {
                                        onSelected?.invoke(idx)
                                    }.padding(
                                        horizontal = Spacing.s,
                                        vertical = Spacing.s,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            ) {
                                item.leadingContent?.invoke()
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.label,
                                    style =
                                    item.customLabelStyle
                                        ?: MaterialTheme.typography.bodyLarge,
                                )
                                item.trailingContent?.invoke()
                            }
                        }
                    }
                }
            }
        },
    )
}