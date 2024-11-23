package com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccountsBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    state: SheetState = rememberModalBottomSheetState(),
    parent: Screen,
    onDismiss: ((Boolean) -> Unit)? = null,
) {
    val model = parent.getScreenModel<ManageAccountsMviModel>()
    val uiState by model.uiState.collectAsState()
    var indexToDelete by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    ManageAccountsMviModel.Effect.Close -> {
                        sheetScope
                            .launch {
                                state.hide()
                            }.invokeOnCompletion {
                                onDismiss?.invoke(false)
                            }
                    }
                }
            }.launchIn(this)
    }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = state,
        onDismissRequest = {
            onDismiss?.invoke(false)
        },
    ) {
        Column(
            modifier = Modifier.padding(bottom = Spacing.xs),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = LocalStrings.current.manageAccountsTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            LazyColumn(
                modifier = Modifier.padding(top = Spacing.m).height(250.dp),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                itemsIndexed(uiState.accounts) { idx, account ->
                    AccountItem(
                        modifier = Modifier.fillMaxWidth(),
                        account = account,
                        autoLoadImages = uiState.autoLoadImages,
                        onClick = {
                            model.reduce(ManageAccountsMviModel.Intent.SwitchAccount(idx))
                        },
                        options =
                            buildList {
                                this +=
                                    Option(
                                        OptionId.Delete,
                                        LocalStrings.current.commentActionDelete,
                                    )
                            },
                        onOptionSelected = { optionId ->
                            when (optionId) {
                                OptionId.Delete -> {
                                    indexToDelete = idx
                                }

                                else -> Unit
                            }
                        },
                    )
                }
            }

            Button(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = Spacing.m),
                onClick = {
                    sheetScope
                        .launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onDismiss?.invoke(true)
                        }
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                    Text(LocalStrings.current.manageAccountsButtonAdd)
                }
            }
        }

        indexToDelete?.also { idx ->
            AlertDialog(
                onDismissRequest = {
                    indexToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            indexToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(ManageAccountsMviModel.Intent.DeleteAccount(idx))
                            indexToDelete = null
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
