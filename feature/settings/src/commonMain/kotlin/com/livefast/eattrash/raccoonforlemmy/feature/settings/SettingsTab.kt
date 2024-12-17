package com.livefast.eattrash.raccoonforlemmy.feature.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInt
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsScreen

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            val title = LocalStrings.current.navigationSettings

            return TabOptions(
                index = TabNavigationSection.Settings.toInt().toUShort(),
                title = title,
                icon = icon,
            )
        }

    @Composable
    override fun Content() {
        Navigator(SettingsScreen())
    }
}
