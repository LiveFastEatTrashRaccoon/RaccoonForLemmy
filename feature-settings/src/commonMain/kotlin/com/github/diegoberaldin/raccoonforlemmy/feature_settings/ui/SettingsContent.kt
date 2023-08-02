package com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.racconforlemmy.core_utils.toLanguageName
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun SettingsContent(
    uiState: SettingsScreenMviModel.UiState,
    onSelectTheme: () -> Unit,
    onSelectLanguage: () -> Unit,
    onSelectListingType: () -> Unit,
    onSelectPostSortType: () -> Unit,
    onSelectCommentSortType: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        // theme
        SettingsRow(
            title = stringResource(MR.strings.settings_ui_theme),
            value = uiState.currentTheme.toReadableName(),
            onTap = {
                onSelectTheme()
            },
        )

        // language
        SettingsRow(
            title = stringResource(MR.strings.settings_language),
            value = uiState.lang.toLanguageName(),
            onTap = {
                onSelectLanguage()
            },
        )

        // default listing type
        SettingsRow(
            title = stringResource(MR.strings.settings_default_listing_type),
            value = uiState.defaultListingType.toReadableName(),
            onTap = {
                onSelectListingType()
            },
        )

        // default post sort type
        SettingsRow(
            title = stringResource(MR.strings.settings_default_post_sort_type),
            value = uiState.defaultPostSortType.toReadableName(),
            onTap = {
                onSelectPostSortType()
            },
        )

        // default comment sort type
        SettingsRow(
            title = stringResource(MR.strings.settings_default_comment_sort_type),
            value = uiState.defaultCommentSortType.toReadableName(),
            onTap = {
                onSelectCommentSortType()
            },
        )
    }
}
