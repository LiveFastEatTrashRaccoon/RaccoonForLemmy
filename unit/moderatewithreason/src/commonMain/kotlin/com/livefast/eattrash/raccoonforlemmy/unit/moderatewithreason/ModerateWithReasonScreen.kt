package com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.safeImePadding
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di.ModerateWithReasonMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class ModerateWithReasonScreen(
    private val actionId: Int,
    private val contentId: Long,
) : Screen {
    override val key: ScreenKey
        get() = super.key + "$actionId-$contentId"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: ModerateWithReasonMviModel =
            rememberScreenModel(
                arg =
                    ModerateWithReasonMviModelParams(
                        actionId = actionId,
                        contentId = contentId,
                    ),
            )
        val uiState by model.uiState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val genericError = LocalStrings.current.messageGenericError
        val successMessage = LocalStrings.current.messageOperationSuccessful
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
        val focusManager = LocalFocusManager.current

        LaunchedEffect(model) {
            model.effects
                .onEach {
                    when (it) {
                        is ModerateWithReasonMviModel.Effect.Failure -> {
                            snackbarHostState.showSnackbar(it.message ?: genericError)
                        }

                        ModerateWithReasonMviModel.Effect.Success -> {
                            navigationCoordinator.showGlobalMessage(message = successMessage, delay = 1.seconds)
                            navigationCoordinator.popScreen()
                        }
                    }
                }.launchIn(this)
        }

        Scaffold(
            modifier = Modifier.navigationBarsPadding().safeImePadding(),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigationCoordinator.popScreen()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    },
                    title = {
                        val title =
                            when (uiState.action) {
                                is ModerateWithReasonAction.HideCommunity -> LocalStrings.current.postActionHide
                                is ModerateWithReasonAction.PurgeComment -> LocalStrings.current.adminActionPurge
                                is ModerateWithReasonAction.PurgeCommunity -> LocalStrings.current.adminActionPurge
                                is ModerateWithReasonAction.PurgePost -> LocalStrings.current.adminActionPurge
                                is ModerateWithReasonAction.PurgeUser -> LocalStrings.current.adminActionPurge
                                is ModerateWithReasonAction.RemoveComment -> LocalStrings.current.modActionRemove
                                is ModerateWithReasonAction.RemovePost -> LocalStrings.current.modActionRemove
                                is ModerateWithReasonAction.ReportComment -> LocalStrings.current.createReportTitleComment
                                is ModerateWithReasonAction.ReportPost -> LocalStrings.current.createReportTitlePost
                            }
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    actions = {
                        IconButton(
                            modifier = Modifier.padding(horizontal = Spacing.xs),
                            onClick = {
                                focusManager.clearFocus()
                                model.reduce(ModerateWithReasonMviModel.Intent.Submit)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        snackbarData = data,
                    )
                }
            },
        ) { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).consumeWindowInsets(padding)
                        .safeImePadding(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val commentFocusRequester = remember { FocusRequester() }
                TextField(
                    modifier =
                        Modifier
                            .focusRequester(commentFocusRequester)
                            .heightIn(min = 300.dp, max = 500.dp)
                            .fillMaxWidth(),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                    label = {
                        Text(text = LocalStrings.current.createReportPlaceholder)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    value = uiState.text,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrectEnabled = true,
                        ),
                    onValueChange = { value ->
                        model.reduce(ModerateWithReasonMviModel.Intent.SetText(value))
                    },
                    isError = uiState.textError != null,
                    supportingText = {
                        val error = uiState.textError
                        if (error != null) {
                            Text(
                                text = error.toReadableMessage(),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )
                Spacer(Modifier.height(Spacing.xxl))
            }

            if (uiState.loading) {
                ProgressHud()
            }
        }
    }
}
