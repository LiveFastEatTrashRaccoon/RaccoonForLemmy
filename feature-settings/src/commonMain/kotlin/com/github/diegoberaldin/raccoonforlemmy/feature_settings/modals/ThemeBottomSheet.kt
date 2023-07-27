package com.github.diegoberaldin.raccoonforlemmy.feature_settings.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.github.diegoberaldin.racconforlemmy.core_utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui.toIcon
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ThemeBottomSheet(
    onDismiss: (ThemeState) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(Spacing.s),
        verticalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        Text(
            modifier = Modifier.padding(start = Spacing.s, top = Spacing.s),
            text = stringResource(MR.strings.settings_ui_theme),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        val values = listOf(
            ThemeState.Light,
            ThemeState.Dark,
            ThemeState.Black,
        )
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)
        ) {
            for (value in values) {
                Row(modifier = Modifier.padding(Spacing.s).onClick {
                    onDismiss(value)
                }) {
                    Text(
                        text = value.toReadableName(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        imageVector = value.toIcon(),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}