package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent

@Composable
fun SettingsTextualInfo(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String = "",
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onEdit: (() -> Unit)? = null,
) {
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(CornerSize.xxl))
                .fillMaxWidth()
                .then(
                    if (onEdit != null) {
                        Modifier.clickable {
                            onEdit.invoke()
                        }
                    } else {
                        Modifier
                    },
                ).padding(
                    vertical = Spacing.s,
                    horizontal = Spacing.m,
                ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = ancillaryColor,
            )
            CustomizedContent(ContentFontClass.Body) {
                Text(
                    text = value,
                    style = valueStyle,
                    color = fullColor,
                )
            }
        }
    }
}
