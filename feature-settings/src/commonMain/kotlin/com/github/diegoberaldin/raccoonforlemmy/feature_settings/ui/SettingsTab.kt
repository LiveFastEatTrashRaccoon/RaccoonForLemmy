package com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.di.getSettingsScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.modals.ThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SettingsTab : Tab {

    private val bottomSheetChannel = Channel<(@Composable () -> Unit)?>()
    val bottomSheetFlow = bottomSheetChannel.receiveAsFlow()

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MR.strings.navigation_settings)
            val icon = rememberVectorPainter(Icons.Default.Settings)

            return remember {
                TabOptions(
                    index = 4u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getSettingsScreenModel() }
        model.bindToLifecycle(key)

        val uiState by model.uiState.collectAsState()

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                TopAppBar({
                    Text(
                        text = stringResource(MR.strings.navigation_settings),
                        style = MaterialTheme.typography.titleMedium
                    )
                })
            },
        ) {
            Box(
                modifier = Modifier.padding(it),
            ) {
                SettingsContent(
                    uiState = uiState,
                    onSelectTheme = {
                        bottomSheetChannel.trySend {
                            ThemeBottomSheet { newValue ->
                                model.reduce(SettingsScreenMviModel.Intent.ChangeTheme(newValue))
                                bottomSheetChannel.trySend(null)
                            }
                        }
                    }
                )
            }
        }
    }
}
