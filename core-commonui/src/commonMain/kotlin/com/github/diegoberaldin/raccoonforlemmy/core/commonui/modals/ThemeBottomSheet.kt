package com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals

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
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toIcon
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class ThemeBottomSheet(
    private val onSelected: (ThemeState) -> Unit,
    private val onHide: () -> Unit,
) : Screen {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = Spacing.s,
                    start = Spacing.s,
                    end = Spacing.s,
                    bottom = Spacing.m,
                ),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
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
                verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
            ) {
                for (value in values) {
                    Row(
                        modifier = Modifier.padding(Spacing.s).onClick {
                            onSelected(value)
                            onHide()
                        },
                    ) {
                        Text(
                            text = value.toReadableName(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            imageVector = value.toIcon(),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    }
                }
            }
        }
    }
}
