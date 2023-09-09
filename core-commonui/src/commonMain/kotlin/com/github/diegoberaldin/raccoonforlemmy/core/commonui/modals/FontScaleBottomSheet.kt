package com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.FontScale
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.scaleFactor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.BottomSheetHandle
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class FontScaleBottomSheet(
    private val onSelected: (Float) -> Unit,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetHandle()
                Text(
                    modifier = Modifier.padding(start = Spacing.s, top = Spacing.s),
                    text = stringResource(MR.strings.settings_content_font_scale),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                val values = listOf(
                    FontScale.Largest,
                    FontScale.Larger,
                    FontScale.Large,
                    FontScale.Normal,
                    FontScale.Small,
                    FontScale.Smaller,
                    FontScale.Smallest,
                )
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
                ) {
                    for (value in values) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Spacing.s,
                                vertical = Spacing.m,
                            )
                                .fillMaxWidth()
                                .onClick {
                                    onSelected(value.scaleFactor)
                                    onHide()
                                },
                        ) {
                            val originalFontSize = MaterialTheme.typography.bodyLarge.fontSize
                            Text(
                                text = value.toReadableName(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = originalFontSize * value.scaleFactor,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}
