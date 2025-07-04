package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewState

@Composable
fun PostCardEmbeddedWebView(
    url: String,
    modifier: Modifier = Modifier,
    blurred: Boolean = false,
    autoLoadImages: Boolean = true,
    onOpen: (() -> Unit)? = null,
) {
    if (url.isEmpty()) {
        return
    }
    val state =
        rememberWebViewState(
            url = url,
        )

    LaunchedEffect(Unit) {
        state.settings.javaScriptEnabled = true
        state.settings.androidSettings.supportZoom = true
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (blurred) {
            Column(
                modifier = Modifier.padding(vertical = Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Text(
                    text = LocalStrings.current.messageVideoNsfw,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(
                    onClick = {
                        onOpen?.invoke()
                    },
                ) {
                    Text(
                        text = LocalStrings.current.buttonLoad,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        } else {
            var shouldBeRendered by remember(autoLoadImages) { mutableStateOf(autoLoadImages) }
            if (shouldBeRendered) {
                WebView(
                    modifier = Modifier.fillMaxWidth().aspectRatio(9f / 16f),
                    state = state,
                )
            } else {
                Button(
                    modifier = Modifier.padding(vertical = Spacing.s),
                    onClick = {
                        shouldBeRendered = true
                    },
                ) {
                    Text(
                        text = LocalStrings.current.buttonLoad,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
