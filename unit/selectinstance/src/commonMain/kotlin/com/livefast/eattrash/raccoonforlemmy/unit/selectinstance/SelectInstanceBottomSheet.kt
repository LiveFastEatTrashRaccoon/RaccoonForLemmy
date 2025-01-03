package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.components.SelectInstanceItem
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.dialog.ChangeInstanceDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInstanceBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    state: SheetState = rememberModalBottomSheetState(),
    parent: Screen,
    onSelected: ((String?) -> Unit)? = null,
) {
    val model: SelectInstanceMviModel = parent.rememberScreenModel()
    val uiState by model.uiState.collectAsState()
    var changeInstanceDialogOpen by remember {
        mutableStateOf(false)
    }

    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState =
        rememberReorderableLazyListState(lazyListState) { from, to ->
            model.reduce(
                SelectInstanceMviModel.Intent.SwapIntances(
                    from = from.index - 1,
                    to = to.index - 1,
                ),
            )
        }
    var instanceToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(model) {
        model.effects
            .onEach { evt ->
                when (evt) {
                    SelectInstanceMviModel.Effect.CloseDialog -> {
                        changeInstanceDialogOpen = false
                    }

                    is SelectInstanceMviModel.Effect.Confirm -> {
                        sheetScope
                            .launch {
                                state.hide()
                            }.invokeOnCompletion {
                                onSelected?.invoke(evt.instance)
                            }
                    }
                }
            }.launchIn(this)
    }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = state,
        onDismissRequest = {
            onSelected?.invoke(null)
        },
    ) {
        Column(
            modifier = Modifier.padding(bottom = Spacing.xs),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = LocalStrings.current.dialogTitleChangeInstance,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        changeInstanceDialogOpen = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = LocalStrings.current.buttonAdd,
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.xs))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                if (uiState.instances.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs),
                            textAlign = TextAlign.Center,
                            text = LocalStrings.current.messageEmptyList,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                } else {
                    item {
                        // workaround for https://github.com/Calvin-LL/Reorderable/issues/4
                        ReorderableItem(
                            state = reorderableLazyColumnState,
                            key = "dummy",
                            enabled = false,
                            modifier = Modifier.fillMaxWidth().height(Dp.Hairline),
                        ) {}
                    }
                }
                items(
                    items = uiState.instances,
                    key = { it },
                ) { instance ->
                    ReorderableItem(
                        state = reorderableLazyColumnState,
                        key = instance,
                    ) { isDragging ->
                        val isActive = instance == uiState.currentInstance
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                        Surface(
                            color = Color.Transparent,
                            tonalElevation = 1.5.dp,
                            shadowElevation = elevation,
                        ) {
                            SelectInstanceItem(
                                instance = instance,
                                isActive = isActive,
                                onDragStarted = {
                                    model.reduce(SelectInstanceMviModel.Intent.HapticIndication)
                                },
                                onClick = {
                                    model.reduce(
                                        SelectInstanceMviModel.Intent.SelectInstance(instance),
                                    )
                                },
                                reorderableScope = this,
                                options =
                                    buildList {
                                        if (!isActive) {
                                            this +=
                                                Option(
                                                    OptionId.Delete,
                                                    LocalStrings.current.commentActionDelete,
                                                )
                                        }
                                    },
                                onOptionSelected = { optionId ->
                                    when (optionId) {
                                        OptionId.Delete -> {
                                            instanceToDelete = instance
                                        }

                                        else -> Unit
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        if (changeInstanceDialogOpen) {
            ChangeInstanceDialog(
                instanceName = uiState.changeInstanceName,
                instanceNameError = uiState.changeInstanceNameError,
                loading = uiState.changeInstanceLoading,
                onClose = {
                    changeInstanceDialogOpen = false
                },
                onChangeInstanceName = { value ->
                    model.reduce(SelectInstanceMviModel.Intent.ChangeInstanceName(value))
                },
                onSubmit = {
                    model.reduce(SelectInstanceMviModel.Intent.SubmitChangeInstanceDialog)
                },
            )
        }

        instanceToDelete?.also { instance ->
            AlertDialog(
                onDismissRequest = {
                    instanceToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            instanceToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(SelectInstanceMviModel.Intent.DeleteInstance(instance))
                            instanceToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
            )
        }
    }
}
