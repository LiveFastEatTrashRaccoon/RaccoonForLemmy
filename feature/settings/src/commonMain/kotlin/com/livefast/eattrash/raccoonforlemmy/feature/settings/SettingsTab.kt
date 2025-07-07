package com.livefast.eattrash.raccoonforlemmy.feature.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsScreen

@Composable
fun SettingsTab(model: SettingsMviModel, scrollState: ScrollState, modifier: Modifier = Modifier) {
    SettingsScreen(
        modifier = modifier,
        model = model,
        scrollState = scrollState,
    )
}
