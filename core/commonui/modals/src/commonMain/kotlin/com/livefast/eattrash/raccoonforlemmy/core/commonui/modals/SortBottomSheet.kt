package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal sealed interface SortBottomSheetLevel {
    data object Main : SortBottomSheetLevel

    data object Top : SortBottomSheetLevel
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    values: List<SortType>,
    expandTop: Boolean = false,
    onSelected: ((SortType?) -> Unit)? = null,
) {
    ModalBottomSheet(
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = sheetState,
        onDismissRequest = {
            onSelected?.invoke(null)
        },
    ) {
        Column(
            modifier = Modifier.padding(bottom = Spacing.xs),
        ) {
            var level by remember { mutableStateOf<SortBottomSheetLevel>(SortBottomSheetLevel.Main) }
            Crossfade(
                targetState = level,
            ) { currentLevel ->
                when (currentLevel) {
                    SortBottomSheetLevel.Main -> {
                        SortBottomSheetMain(
                            sheetScope = sheetScope,
                            sheetState = sheetState,
                            values = values,
                            expandTop = expandTop,
                            onSelected = onSelected,
                            onNavigateDown = {
                                level = SortBottomSheetLevel.Top
                            },
                        )
                    }

                    SortBottomSheetLevel.Top -> {
                        SortBottomSheetTop(
                            sheetScope = sheetScope,
                            sheetState = sheetState,
                            onSelected = onSelected,
                            onNavigateUp = {
                                level = SortBottomSheetLevel.Main
                            },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheetMain(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    values: List<SortType>,
    expandTop: Boolean = false,
    onNavigateDown: () -> Unit,
    onSelected: ((SortType?) -> Unit)? = null,
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = LocalStrings.current.homeSortTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        LazyColumn {
            items(values) { value ->
                SortBottomSheetValueRow(
                    value = value,
                    expandTop = expandTop,
                    onSelected = {
                        if (value == SortType.Top.Generic && expandTop) {
                            onNavigateDown()
                        } else {
                            sheetScope
                                .launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    onSelected?.invoke(value)
                                }
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheetTop(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    values: List<SortType> =
        listOf(
            SortType.Top.PastHour,
            SortType.Top.Past6Hours,
            SortType.Top.Past12Hours,
            SortType.Top.Day,
            SortType.Top.Week,
            SortType.Top.Month,
            SortType.Top.Year,
            SortType.Top.All,
        ),
    onNavigateUp: () -> Unit,
    onSelected: ((SortType?) -> Unit)? = null,
) {
    Column {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = SortType.Top.Generic.toReadableName() + "…",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Row(
                modifier = Modifier.padding(start = Spacing.xxs),
            ) {
                IconButton(
                    onClick = {
                        onNavigateUp()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(Spacing.xs))
        LazyColumn {
            items(values) { value ->
                SortBottomSheetValueRow(
                    value = value,
                    onSelected = {
                        sheetScope
                            .launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                onSelected?.invoke(value)
                            }
                    },
                )
            }
        }
    }
}

@Composable
private fun SortBottomSheetValueRow(
    value: SortType,
    expandTop: Boolean = false,
    onSelected: (() -> Unit)?,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.xl))
                .clickable { onSelected?.invoke() }
                .padding(
                    horizontal = Spacing.m,
                    vertical = Spacing.s,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
    ) {
        val name =
            buildString {
                append(value.toReadableName())
                if (value == SortType.Top.Generic && expandTop) {
                    append("…")
                }
            }
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Icon(
            imageVector =
                if (value == SortType.Top.Generic && expandTop) {
                    Icons.Default.ChevronRight
                } else {
                    value.toIcon()
                },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}
