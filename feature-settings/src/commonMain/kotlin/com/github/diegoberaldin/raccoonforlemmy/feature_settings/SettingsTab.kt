package com.github.diegoberaldin.raccoonforlemmy.feature_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

object SettingsTab : Tab {

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

    @Composable
    override fun Content() {
        val model = rememberScreenModel { getSettingsScreenModel() }
        model.bindToLifecycle(key)

        val uiState by model.uiState.collectAsState()

        Column(modifier = Modifier.padding(Spacing.xs)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(MR.strings.settings_dark_theme)
                )
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked = uiState.darkTheme,
                    onCheckedChange = {
                        model.reduce(SettingsScreenMviModel.Intent.EnableDarkMode(it))
                    }
                )
            }
        }
    }
}