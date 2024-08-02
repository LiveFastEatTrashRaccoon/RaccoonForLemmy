package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectTabNavigationSectionBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInt
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.rememberCallback
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.composable.ConfigureAddAction
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.composable.ConfigureNavBarItem
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

class ConfigureNavBarScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ConfigureNavBarMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val lazyListState = rememberLazyListState()
        val reorderableLazyColumnState =
            rememberReorderableLazyListState(lazyListState) { from, to ->
                model.reduce(
                    ConfigureNavBarMviModel.Intent.SwapItems(
                        from = from.index - 1,
                        to = to.index - 1,
                    ),
                )
            }
        var confirmBackWithUnsavedChangesDialog by remember { mutableStateOf(false) }

        DisposableEffect(key) {
            navigationCoordinator.setCanGoBackCallback {
                if (uiState.hasUnsavedChanges) {
                    confirmBackWithUnsavedChangesDialog = true
                    return@setCanGoBackCallback false
                }
                true
            }
            onDispose {
                navigationCoordinator.setCanGoBackCallback(null)
            }
        }

        Scaffold(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.settingsItemConfigureBottomNavigationBar,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
                            Image(
                                modifier =
                                    Modifier.onClick(
                                        onClick = {
                                            if (uiState.hasUnsavedChanges) {
                                                confirmBackWithUnsavedChangesDialog = true
                                            } else {
                                                navigationCoordinator.popScreen()
                                            }
                                        },
                                    ),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            contentPadding =
                                PaddingValues(
                                    horizontal = Spacing.xs,
                                    vertical = Spacing.xxs,
                                ),
                            onClick = {
                                model.reduce(ConfigureNavBarMviModel.Intent.Reset)
                            },
                        ) {
                            Text(
                                text = LocalStrings.current.buttonReset,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    },
                )
            },
        ) { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).navigationBarsPadding()
                        .then(
                            if (settings.hideNavigationBarWhileScrolling) {
                                Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                            } else {
                                Modifier
                            },
                        ),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    if (uiState.sections.isEmpty()) {
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
                        items = uiState.sections,
                        key = { it.toInt().toString() },
                    ) { section ->
                        ReorderableItem(
                            state = reorderableLazyColumnState,
                            key = section.toInt().toString(),
                        ) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                            Surface(
                                shadowElevation = elevation,
                            ) {
                                ConfigureNavBarItem(
                                    reorderableScope = this,
                                    title = section.toReadableName(),
                                    onDragStarted =
                                        rememberCallback(model) {
                                            model.reduce(ConfigureNavBarMviModel.Intent.HapticFeedback)
                                        },
                                    onDelete =
                                        rememberCallback(model) {
                                            model.reduce(ConfigureNavBarMviModel.Intent.Delete(section))
                                        },
                                )
                            }
                        }
                    }
                    if (uiState.availableSections.isNotEmpty() && uiState.sections.size < 5) {
                        item {
                            ConfigureAddAction(
                                onAdd =
                                    rememberCallback {
                                        val available = model.uiState.value.availableSections
                                        val sheet =
                                            SelectTabNavigationSectionBottomSheet(values = available)
                                        navigationCoordinator.showBottomSheet(sheet)
                                    },
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.m),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        enabled = uiState.hasUnsavedChanges,
                        onClick = {
                            model.reduce(ConfigureNavBarMviModel.Intent.Save)
                        },
                    ) {
                        Text(text = LocalStrings.current.actionSave)
                    }
                }
            }
        }

        if (confirmBackWithUnsavedChangesDialog) {
            AlertDialog(
                onDismissRequest = {
                    confirmBackWithUnsavedChangesDialog = false
                },
                dismissButton = {
                    Button(
                        onClick = {
                            confirmBackWithUnsavedChangesDialog = false
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonNoStay)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            confirmBackWithUnsavedChangesDialog = false
                            navigationCoordinator.popScreen()
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonYesQuit)
                    }
                },
                text = {
                    Text(text = LocalStrings.current.messageUnsavedChanges)
                },
            )
        }
    }
}
