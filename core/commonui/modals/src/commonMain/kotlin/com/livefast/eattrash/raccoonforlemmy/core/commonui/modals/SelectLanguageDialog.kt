package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageDialog(
    modifier: Modifier = Modifier,
    currentLanguageId: Long? = null,
    languages: List<LanguageModel> = emptyList(),
    onSelect: ((Long?) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss?.invoke() },
    ) {
        Column(
            modifier =
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(vertical = Spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = LocalStrings.current.settingsLanguage,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.6f),
            ) {
                items(items = languages, key = { it.id }) { lang ->
                    LanguageItem(
                        name = lang.name,
                        selected = (currentLanguageId ?: 0L) == lang.id,
                        onSelect = {
                            onSelect?.invoke(lang.id)
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.xxs))
            Button(
                onClick = {
                    onDismiss?.invoke()
                },
            ) {
                Text(text = LocalStrings.current.buttonClose)
            }
        }
    }
}

@Composable
private fun LanguageItem(
    name: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onSelect: (() -> Unit)? = null,
) {
    Row(
        modifier =
        modifier
            .clip(RoundedCornerShape(CornerSize.xxl))
            .fillMaxWidth()
            .onClick(
                onClick = {
                    onSelect?.invoke()
                },
            ).padding(
                horizontal = Spacing.m,
                vertical = Spacing.s,
            ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))

        if (selected) {
            RadioButton(
                selected = true,
                onClick = null,
            )
        }
    }
}
